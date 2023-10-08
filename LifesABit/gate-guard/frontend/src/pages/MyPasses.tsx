import React, { useEffect, useRef, useState } from 'react';
import GuardNavbar from "../components/GuardNavbar";
import "../styles/MyPasses.scss";
import "../styles/NewPassModal.scss";
import { Button, Modal, Form, Row, Col, } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faAdd } from '@fortawesome/free-solid-svg-icons';
import PassComponent from "../components/PassComponent";
import '../styles/PassComponentStyles.scss';
import connector from '../utils/axiosConfig';
import { ErrorMessage } from '@hookform/error-message';
import { toast } from 'react-toastify';
// import Switch from '@mui/material/Switch';
//import Switch from "react-switch";

interface Pass {
  passID?: string;
  firstName?: string;
  lastName?: string;
  usageBased?: boolean;//better name?
  expirationDate?: number;
  usesTotal?: number;
  usesLeft?: number;
  email?: string;
}

interface CreatePassReq {
  usageBased?: boolean;
  firstName?: string;
  lastName?: string;
  email?: string;
  expirationDate?: string | number;
  usesLeft?: number;
  usesTotal?: number;
}

interface CreatePassResp {
  success?: boolean;
  message?: string;
  passID?: string;
}

interface PassListResp {
  passList: Pass[];
}

interface PassListReq {
  sessionKey: string;
}

interface RevokePassReq {
  passID?: string;
}

export const nameRegEx = new RegExp(/^([A-Za-z]*)$/);
const usesRegEx = new RegExp(/^[1-9][0-9]*$/);
export const phoneRegEx = new RegExp(/^([^A-Z^a-z]{1,14})$/);
export const emailRegEx = new RegExp(/^[\w-\.\+]+@([\w-]+\.)+[\w-]{2,4}$/);
export const dateRegEx = new RegExp(/^\d\d\d\d-\d\d-\d\d$/);

const MyPasses: React.FC = (): JSX.Element => {
  const { register, resetField, handleSubmit, watch, formState: { errors } } = useForm({criteriaMode: "all"});
  const [passList, setPassList] = useState<Pass[]>([]);
  const [newPassModalIsOpen, setNewPassModalIsOpen] = useState(false);
  const [usesPass, setUsesPass] = useState<boolean>(false);
  const [notLoggedIn, setNotLoggedIn] = useState<boolean>(false);
  // const [refreshPasses, setRefreshPasses] = useState<boolean>(false);

  const OnSubmitNewPass = async (passData: CreatePassReq) => {
    setNewPassModalIsOpen(false);
    if (passData.usesTotal) {
      passData.usesLeft = passData.usesTotal;
    } else {
      passData.usesTotal = -1;
      passData.usesLeft = -1;
    }
    passData.expirationDate = Date.parse((passData.expirationDate as string) + "T00:00:00");
    try {
      await connector.post('create-pass', passData)
        .then((result) => {
          if (result.status == 200) {
            if (result.data.success) {
              let newPass: Pass = {
                passID: result.data.passID,
                firstName: passData.firstName!,
                lastName: passData.lastName!,
                usageBased: passData.usageBased!,
                expirationDate: (passData.expirationDate as number),
                usesTotal: passData.usesTotal!,
                usesLeft: passData.usesLeft!,
                email: passData.email!
              };
              setPassList(passList => [...passList, newPass]);
            } else {
              toast.error(result.data.message);
            }
          }
        });
    } catch (e: any) {
      console.log(e);
    }
    resetField("firstName");
    resetField("lastName");
    resetField("email");
    resetField("usesTotal");
    resetField("expirationDate");
    return {};
    setNewPassModalIsOpen(false);
    //TODO Clear Form 
  }


  //Base notification
  const loadPasses = async (): Promise<PassListResp> => {
    try {
        await connector.post('load-passes', {})
        .then((result) => {
          setPassList(result.data.passList);
        });
    } catch (e: any) {
      if (e.message.includes("401")) {
        setNotLoggedIn(true);
      }
    }
    return { passList: [] };
  }

  const revokePass = async (passID: string) => {
    const tempReq: RevokePassReq = {
      passID: passID
    };
    try {
      await connector.post('revoke-pass', tempReq)
        .then((result) => {
          if (result.status == 200) {
            let newList = [];
            for (let i: number = 0; i < passList.length; i++) {
              if (passList[i].passID == passID) {
                continue;
              }
              newList[i] = passList[i];
            }
            setPassList(newList);
          }
        });
    } catch (e: any) {
      console.log(e);
    }
    return {};
  }
  
//Base notification
  const renderPassList = (passList: Pass[]): JSX.Element => {
    var list: JSX.Element[] = [];
    passList.forEach((item) => {
      list.push(<PassComponent 
        // data-testid="HI"
        firstName={item.firstName!}
        lastName={item.lastName!}
        email={item.email!}
        expirationDate={item.expirationDate!}
        usesLeft={item.usesLeft}
        usesTotal={item.usesTotal}
        usageBased={item.usageBased}
        passID={item.passID}
        revokePassFunc={revokePass} 
        loadPasses={loadPasses}
        clearPassesFunc={() => setPassList([])}
        />);
    });
    return <>{list}</>
  }

  //moment("20111031", "YYYYMMDD").fromNow();
  

  // function createTestList(): void {
  //   const tempPass1: Pass = {
  //       passID: "12345",
  //       firstName: "Vaughn", 
  //       lastName: "Eugenio", 
  //       expirationDate: "11/27/2022",
  //       email: "example@email.com",
  //       usageBased: false//better name?
  //   }
  //   passList.push(
  //     tempPass1
  //   );

  // }

  useEffect(() => {
    loadPasses();
  }, []);


  useEffect(() => {
    resetField("expirationDate")
    resetField("usesTotal")
    //clear expr date or uses
  }, [usesPass]);

  return (
    <>
      <div className="mypasses">
        <GuardNavbar />
        <header className="mypasses-header">
          <h2>MyPasses</h2>
        </header>
        <body className="mypasses-body">
          {/* <h2>Hello</h2>
          <PassComponent 
            firstName={"Vaughn"} 
            lastName={"Eugenio"} 
            expirationDate={"7/5/22"} 
            usesLeft={5} 
            usesTotal={5} /> */}

          {/* {renderPassList(passList)} */}
          <div className="passList-container">

            {notLoggedIn && <h3 className="notLoggedInMessage">You are not currently logged in, or your session has expired. <br/> Please log in to continue.</h3>}
            {renderPassList(passList)}


          </div>

          {/* <PassComponent 
            firstName={"Vaughn"} 
            lastName={"Eugenio"} 
            expirationDate={"7/5/22"} 
            usesLeft={5} 
            usesTotal={5} />*/}
          <button className="createPass-btn" onClick={() => setNewPassModalIsOpen(true)}>
            {/* <FontAwesomeIcon icon={fa-solid fa-plus} />   WHY NO WORK? */}
            <FontAwesomeIcon className="createPass-icon" icon={faAdd} onClick={() => setNewPassModalIsOpen(true)} />
          </button>


        </body>
        <Modal
          show={newPassModalIsOpen}
          onHide={() => setNewPassModalIsOpen(false)}
          className="newPassModal"
        >
          <Form id="newPassForm" onSubmit={handleSubmit(OnSubmitNewPass)}>
            <Modal.Header closeButton>
              <Modal.Title>New pass</Modal.Title>
            </Modal.Header>
            <Modal.Body>
              <Row>
                <Col>
                  <Form.Label>First Name</Form.Label>
                  <Form.Control className="newPassInput"  type="text" maxLength={36} placeholder="" 
                    {...register("firstName",{ 
                      required: "Required",
                      pattern: {
                        value: nameRegEx,
                        message: "Letters only"
                      },
                      })}>
                  </Form.Control>
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
              </Row>
              <Row>
                <Col>
                  <Form.Label>Last Name</Form.Label>
                  <Form.Control className="newPassInput"  type="text" maxLength={36} placeholder="" 
                    {...register("lastName",{ 
                      required: "Required",
                      pattern: {
                        value: nameRegEx,
                        message: "Letters only"
                      },
                      })}>
                  </Form.Control>
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
              <Row>
                <Col>
                  <Form.Label>E-mail address</Form.Label>
                  <Form.Control className="newPassInput" type="text" placeholder="" maxLength={36} 
                  {...register("email",{
                  required: "Required", 
                  pattern: {
                    value: emailRegEx ,
                    message: "Invalid email address"
                  },
                  })}>   
                  </Form.Control>
                  <ErrorMessage 
                    errors={errors} 
                    name="email" 
                    render={({ messages }) =>
                    messages &&
                    Object.entries(messages).map(([type, message]) => (
                      <p className="redText" key={type}>{message}</p>
                    ))
                  }/>
                </Col>
              </Row>
            
              <Form.Check label="Usage-based" type="switch" {...register("usageBased")} checked={usesPass} onChange={() => setUsesPass(!usesPass)}></Form.Check>
              
              {usesPass ?
                <>
                  <Form.Label>Total uses</Form.Label>
                  {/* TODO: max based on admin */}
                  <Form.Control className="newPassInput" type="text" placeholder="e.g. 5" 
                  {...register("usesTotal",{
                    pattern: {
                      value: usesRegEx,
                      message: "Numbers only"
                    }
                  })}>
                  </Form.Control>
                  <ErrorMessage 
                    errors={errors} 
                    name="email" 
                    render={({ messages }) =>
                    messages &&
                    Object.entries(messages).map(([type, message]) => (
                      <p className="redText" key={type}>{message}</p>
                    ))
                  }/>
                </> :
                <>
                  <Form.Label>Expiration</Form.Label>
                  {/* TODO: max date based on admin */}
                  <Form.Control className="newPassInput" type="date" placeholder="MM/DD/YYYY"
                  {...register("expirationDate",{
                  required: "Required", 
                  // validate: (value: any) => (new Date(value) > new Date())
                  validate: {
                    dateValid: inDate => {
                      return (new Date(inDate)> new Date() && dateRegEx.test(inDate))
                    } ,
                    
                  }
                  })}>   
                  </Form.Control>
                  <ErrorMessage
                    errors={errors}
                    name="expirationDate"
                    render={() => <p className='redText'>Invalid Date</p>}
                  />
                  {/* <ErrorMessage 
                    errors={errors} 
                    name="expirationDate" 
                    render={({ messages }) =>
                    messages &&
                    Object.entries(messages).map(([type, message]) => (
                      <p className="redText" key={type}>{message}</p>
                    ))
                  }/> */}
                </>
              }
              {/* <Form.Select aria-label="Expr. type">
              <option>Select Type</option>
              <option value="1">Date</option>
              <option value="2">Uses</option>
            </Form.Select>
            */}

              {/* TODO implement FORM */}
            </Modal.Body>
            <Modal.Footer>
              <Button variant="secondary" onClick={() => setNewPassModalIsOpen(false)}>
                Cancel
              </Button>
              <Button type="submit" className="theme-btn" >
                Create
              </Button>
            </Modal.Footer>
          </Form>
        </Modal>
      </div>
    </>
  );
}

export default MyPasses;