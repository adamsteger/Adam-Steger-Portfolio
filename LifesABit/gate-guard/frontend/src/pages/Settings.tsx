import React, { useEffect } from 'react';
import {useState} from 'react';
import ReactSlider from "react-slider";
import ReactDOM from "react-dom";
import ImageLoader from "react-imageloader";
import styled from "styled-components";
import GuardNavBar from '../components/GuardNavbar';
import Search from './Search';
import '../styles/Settings.scss';
import { Button, Col, Form, Row } from "react-bootstrap";
import { useAppContext } from '..';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import connector from '../utils/axiosConfig';
import { getRandomFirstName, getRandomLastName } from '../utils/RandomNames';
import { nameRegEx, emailRegEx, phoneRegEx } from './MyPasses';
import { ErrorMessage } from '@hookform/error-message';
import { useTheme } from "../utils/useTheme";
import useDarkMode from "use-dark-mode";

interface UserSettings {
  userID: string;
  notifPassUsage: boolean;
  notifPassExpiration: boolean;
  notifPassExpiresSoon: boolean;
  lightMode: boolean;
}

interface LoadUserSettingsRequest {

}

interface LoadUserSettingsResponse {
    userSettings: UserSettings;
    message: string;
}

interface EditUserSettingsRequest {
    notifPassUsage?: boolean;
    notifPassExpiration?: boolean;
    notifPassExpiresSoon?: boolean;
    lightMode?: boolean;
}

interface EditUserSettingsResponse {
    success: boolean;
    message: string;
}

interface EditUserInfoRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  username?: string;
  phone?: string;
}

interface UserInfoResponse {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  phone: string;
  isAdmin: boolean;
  isOwner: boolean;
}

interface EditUserInfoRequest {
  success: boolean;
  message: string;
}

interface AdminSettings {
  userID: string;
  maxPassDuration: number;
  maxPassUsage: number;
  maxPassesPerUser: number;
}

interface LoadAdminSettingsRequest {

}

interface LoadAdminSettingsResponse {
    adminSettings: AdminSettings;
    message: String;
}

interface EditAdminSettingsRequest {
    maxPassDuration?: number;
    maxPassUsage?: number;
    maxPassesPerUser?: number;
}

interface EditAdminSettingsResponse {
    success: boolean;
    message: String;
}


const ThemeBtn = () => {
  const darkMode = useDarkMode(true);
  const theme = useTheme();
  return (
    <Button className="theme-btn" onClick={darkMode.toggle}>
      {theme === "dark-mode" ? "Light mode" : "Dark mode"}
    </Button>
  );
};


const Settings: React.FC = () => {

    //Default Values
    const [maxPassDuration, setMaxPassDuration] = useState<number>(10);
    const [maxPassUsage, setMaxPassUsage] = useState<number>(25);
    const [maxPassesPerUser, setMaxPassesPerUser] = useState<number>(5);
    const { register, handleSubmit, watch, getValues, setValue, formState: { errors } } = useForm({criteriaMode: "all"});
    const [isAdmin, setIsAdmin] = useState<boolean>(false);
    const [isOwner, setIsOwner] = useState<boolean>(false);
    const [emailTaken, setEmailTaken] = useState<boolean>(false);
    const [usernameTaken, setUsernameTaken] = useState<boolean>(false);

    const [lightMode, setLightMode] = useState<boolean>(false);

    const [theme, setTheme] = useState("");

    const pageTheme = useTheme();

    useEffect(() => {
      // const darkMode = useDarkMode(true);
      // darkMode.toggle;
      

      if (lightMode === true) {

        setTheme("light"); //TODO: Send to backend
      } else {
        setTheme("dark"); //TODO: Send to backend
      }
      
  },[lightMode]);






    const appContext = useAppContext();

    const onSubmit = async (data: EditUserSettingsRequest | EditAdminSettingsRequest | EditUserInfoRequest) => {
      let allSuccessful: boolean = true;
      setEmailTaken(false);
      setUsernameTaken(false);
      if (theme === "dark-mode") {
        (data as any).lightMode = false;
      } else {
        (data as any).lightMode = true;
      }
      try {
        await connector.post('edit-user-settings', data)
        .then((result) => {
          if (result.status != 200 || !result.data.success) {
            toast.error("There was an error saving your settings. Please try again later.");
            allSuccessful = false;
            
          }
        });
      } catch (e: any) {
        toast.error("There was an error saving your settings. Please try again later.");
        allSuccessful = false;
      }

      if (isAdmin) {
        try {
          // Flatten the max pass duration into days, instead of weeks, months, etc.
          // This piece of code is horrible and needs to be rewritten as something more sane
          // when someone has the energy
          if ((data as EditAdminSettingsRequest).maxPassDuration) {
            let rawMaxPassDurationNum = (data as EditAdminSettingsRequest).maxPassDuration!;
            let maxPassDurationStr = getDurationNumber(rawMaxPassDurationNum);
            let currMaxPassDurationNum = (maxPassDurationStr === "Infinite" ? -1 : +(maxPassDurationStr.replace(/[a-zA-Z ]+$/, "")));
            if (currMaxPassDurationNum === -1) {
              (data as EditAdminSettingsRequest).maxPassDuration = -1;
            } else {
              let unitStr = maxPassDurationStr.replace(/^[0-9 ]+/, "");
              if (unitStr.toLowerCase().startsWith("day")) {
                (data as EditAdminSettingsRequest).maxPassDuration = currMaxPassDurationNum;
              } else if (unitStr.toLowerCase().startsWith("week")) {
                (data as EditAdminSettingsRequest).maxPassDuration = currMaxPassDurationNum * 7;
              } else if (unitStr.toLowerCase().startsWith("month")) {
                (data as EditAdminSettingsRequest).maxPassDuration = currMaxPassDurationNum * 30;
              }
            }
          }
          await connector.post('edit-settings', data)
          .then((result) => {
            if (result.status != 200 || !result.data.success) {
              toast.error("An error occurred when trying to save changes to site settings.");
              allSuccessful = false;
            }
          });
        } catch (e: any) {
          toast.error("An error occurred when trying to save changes to site settings.");
          allSuccessful = false;
        }
      }

      await connector.post('edit-user-info', data)
        .then((result) => {
          if (result.status != 200 || !result.data.success) {
            toast.error("An error occurred when trying to save changes to contact information.");
            allSuccessful = false;
          }
        }).catch((error) => {
          if (error.response.status == 401) {
            if (error.response.data.message == "This email address is taken. Please choose another.") {
              setEmailTaken(true);
            } else if (error.response.data.message == "This username is taken. Please choose another.") {
              setUsernameTaken(true);
            } else {
              toast.error("An error occurred when trying to save changes to contact information.");
            }
          } else {
            toast.error("An error occurred when trying to save changes to contact information.");
          }
          allSuccessful = false;
        });

      if (allSuccessful) {
        toast.info("Settings updated!");
      }
    }

    const getMaxPassDurationProgression = () => {
      return {backgroundSize: `${maxPassDuration*100/32}% 100%`}
    };

    const getMaxPassUsageLimitProgression = () => {
      return {backgroundSize: `${maxPassUsage*100 / 100}% 100%`}
    };

    const getMaxPassesPerUserLimitProgression = () => {
      return {backgroundSize: `${maxPassesPerUser*100 / 10}% 100%`}
    };



    const getDurationNumber = (number : number) => {
      let result = "";
      if (number < 7 ) {
        result = number === 1 ? "1 day" : number + " days";
      } else if (number < 28) {
        let weeks = Math.floor(number / 7);
        result = weeks === 1 ? "1 week" : weeks + " weeks";
      } else if (number < 30) {
        result = "1 month";
      } else if (number >= 30) {
        return "Infinite";
      }
      return result;
    }

    const getUsageNumber = (number : number) => 
    {
      let result = "";
      if (number === 1 ) {
        result = number + " use";
      } else {
        result = number + " uses";
      }
      return result;
    };

    const getPassesNumber = (number : number) => 
    {
      let result = "";
      if (number === 1 ) {
        result = number + " pass";
      } else {
        result = number + " passes";
      }
      return result;
    };

    const convertDaysToDurationScalar = (number: number): number => {
      if (number === -1 || number > 30) {
        return 30;
      } else if (number < 7) {
        return number;
      } else if (number < 28) {
        let weeks = Math.floor(number / 7);
        return Math.min((weeks * 7), 27);
      } else {
        return 29;
      }
    }

    useEffect(() => {
      // Load user settings
      try {
        connector.post('load-user-settings', {})
        .then((result) => {
          if (result.status == 200) {
            let settingsData = result.data as LoadUserSettingsResponse;
            setValue("lightMode", settingsData.userSettings.lightMode);
            setLightMode(settingsData.userSettings.lightMode);
            setValue("notifPassUsage", settingsData.userSettings.notifPassUsage);
            setValue("notifPassExpiration", settingsData.userSettings.notifPassExpiration);
            setValue("notifPassExpiresSoon", settingsData.userSettings.notifPassExpiresSoon);
          } else {
            toast.error("There was an error loading your settings. Please try again later.");
          }
        });
      } catch (e: any) {
        toast.error("There was an error loading your settings. Please try again later.");
      }

      // Load admin settings
      try {
        connector.post('load-settings', {})
        .then((result) => {
          if (result.status == 200) {
            let settingsData = result.data as LoadAdminSettingsResponse;
            setValue("maxPassDuration", convertDaysToDurationScalar(settingsData.adminSettings.maxPassDuration));
            setMaxPassDuration(convertDaysToDurationScalar(settingsData.adminSettings.maxPassDuration));
            setValue("maxPassUsage", settingsData.adminSettings.maxPassUsage);
            setMaxPassUsage(settingsData.adminSettings.maxPassUsage);
            setValue("maxPassesPerUser", settingsData.adminSettings.maxPassesPerUser);
            setMaxPassesPerUser(settingsData.adminSettings.maxPassesPerUser);
            setIsAdmin(true);
          } else {
            setIsAdmin(false);
          }
        });
      } catch (e: any) {
        setIsAdmin(false);
      }

      // Load contact info
      try {
        connector.post('user-info', {})
        .then((result) => {
          if (result.status == 200) {
            let userInfoData = result.data as UserInfoResponse;
            setValue("firstName", userInfoData.firstName);
            setValue("lastName", userInfoData.lastName);
            setValue("email", userInfoData.email);
            setValue("username", userInfoData.username);
            setValue("phone", userInfoData.phone);
            setIsOwner(userInfoData.isOwner);
          } else {
            toast.error("There was an error loading your settings. Please try again later.");
          }
        });
      } catch (e: any) {
        toast.error("There was an error loading your settings. Please try again later.");
      }
    }, []);

    return (
        <>
        <GuardNavBar/>
          <Form className="settingsForm" onSubmit={handleSubmit(onSubmit)}>

            {/* Admin settings section. This is only rendered if the user is an admin */}
            {appContext.isAdmin &&
              <>
                <div className="contentContainer">
                  <h1 className="textAlignLeft">Site Settings</h1>
                  <hr className="whiteHr"/>
              
                  <div className="bar_input mb-20">
                    <h6 className="textAlignLeft">Maximum Pass Duration</h6>
                    <input type="range" min={1} max={30} value={maxPassDuration} 
                      {...register("maxPassDuration")}
                      onChange={(e) =>  setMaxPassDuration(e.target.valueAsNumber)}
                      style={getMaxPassDurationProgression() }
                    />
                    <span className="ml-20">{getDurationNumber(maxPassDuration)}</span>
                  </div>

                  <div className="bar_input mb-20">
                    <h6 className="textAlignLeft">Maximum Pass Usage Limit</h6>
                    <input type="range" min={1} max={100} value={maxPassUsage} 
                      {...register("maxPassUsageLimit")}
                      onChange={(e) => setMaxPassUsage(e.target.valueAsNumber)}
                      style={getMaxPassUsageLimitProgression()}
                    />
                    <span className="ml-20">{getUsageNumber(maxPassUsage)}</span>
                  </div>

                  <div className="bar_input mb-20">
                    <h6 className="textAlignLeft">Maximum Passes Per User</h6>
                    <input type="range" min={1} max={10} value={maxPassesPerUser}
                      {...register("maxPassesPerUserLimit")}
                      onChange={(e) => setMaxPassesPerUser(e.target.valueAsNumber)}
                      style={getMaxPassesPerUserLimitProgression()}
                    />
                    <span className="ml-20">{getPassesNumber(maxPassesPerUser)}</span>
                  </div>

                  <h3 className="textAlignLeft">User Management</h3>
                  <Search isOwner={isOwner}/>
                </div>
              </>
            }

            {/* User settings section */}
            <div className="contentContainer">
              <h1>User settings</h1>
              <hr className="whiteHr"/>
              <h5>Contact Information</h5>
              <div className="personalInfoDiv">
                <Row>
                  <Col>
                    <Form.Label>First name</Form.Label>
                    <Form.Control type="text" placeholder={getRandomFirstName()} className="nameTextbox" {...register("firstName", { 
                        required: "Required",
                        pattern: {
                          value: nameRegEx,
                          message: "Letters only"
                        },
                      })}/>
                      <ErrorMessage 
                        errors={errors} 
                        name="firstName" 
                        render={({ messages }) =>
                        messages &&
                        Object.entries(messages).map(([type, message]) => (
                          <p className="redText" key={type}>{message}</p>
                        ))
                      }/>
                  </Col>
                  <Col>
                    <Form.Label>Last name</Form.Label>
                    <Form.Control type="text" placeholder={getRandomLastName()} className="nameTextbox" {...register("lastName", { 
                        required: "Required",
                        pattern: {
                          value: nameRegEx,
                          message: "Letters only"
                        },
                      })}/>
                    <ErrorMessage 
                        errors={errors} 
                        name="lastName" 
                        render={({ messages }) =>
                        messages &&
                        Object.entries(messages).map(([type, message]) => (
                          <p className="redText" key={type}>{message}</p>
                        ))
                      }/>
                  </Col>
                </Row>
                <Form.Label>Username</Form.Label>
                <Form.Control type="text" placeholder="username" className="w-300px" maxLength={32} {...register("username", {
                  required: "Required"
                })} onChange={() => {setUsernameTaken(false)}}/>
                <ErrorMessage 
                  errors={errors} 
                  name="username" 
                  render={({ messages }) =>
                  messages &&
                  Object.entries(messages).map(([type, message]) => (
                    <p className="redText" key={type}>{message}</p>
                  ))
                }/>
                {usernameTaken && <p className="redText">This username is taken. Please choose another one.</p>}
                <Form.Label>Email address</Form.Label>
                <Form.Control type="text" placeholder="name@example.com" className="w-300px" maxLength={64} {...register("email", {
                  required: "Required", 
                  pattern: {
                    value: emailRegEx ,
                    message: "Invalid email address"
                  },
                })} onChange={() => {setEmailTaken(false)}}/>
                <ErrorMessage 
                  errors={errors} 
                  name="email" 
                  render={({ messages }) =>
                  messages &&
                  Object.entries(messages).map(([type, message]) => (
                    <p className="redText" key={type}>{message}</p>
                  ))
                }/>
                {emailTaken && <p className="redText">This email address is taken.</p>}
                <Form.Label>Phone number</Form.Label>
                <Form.Control type="text" placeholder="1-555-312-1234" className="w-300px" {...register("phone",{
                  required: "Required", 
                  pattern: {
                    value: phoneRegEx ,
                    message: "Invalid Phone Number"
                  },
                })}/>
                <ErrorMessage 
                  errors={errors} 
                  name="phone" 
                  render={({ messages }) =>
                  messages &&
                  Object.entries(messages).map(([type, message]) => (
                    <p className="redText" key={type}>{message}</p>
                  ))
              }/>
              </div>
              <h5>Interface settings</h5>
              {/* <Form.Check label={lightMode ? "Light mode (not implemented)" : "Dark mode"} {...register("lightMode")} id="lightModeSwitch" type="switch" onChange={(e) => setLightMode(e.target.checked)}/> */}
              <ThemeBtn />
              <h5>Notifications enabled for</h5>
              <Form.Check label="Pass used" {...register("notifPassUsage")}/>
              <Form.Check label="Pass expired" {...register("notifPassExpiration")}/>
              <Form.Check label="Pass about to expire" {...register("notifPassExpiresSoon")}/>
              <hr className="whiteHr"/>
              <div>
                <h6 className="textAlignLeft">
                  <Button className="theme-btn" type="submit">Save</Button>
                </h6>
              </div>
            </div>
          </Form>
      </>
    );
}

export default Settings;