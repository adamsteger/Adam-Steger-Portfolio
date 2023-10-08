import React, { useEffect, useState, useCallback } from 'react';
import GuardNavbar from '../components/GuardNavbar';
import { Button, Modal, Form, Row, Col, Container } from "react-bootstrap";
import { useForm , SubmitHandler} from "react-hook-form";
import { Link, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import '../styles/CreateAccount.scss';
import { faUser, faLock, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import sha512 from 'crypto-js/sha512';
import { toast } from 'react-toastify';
import debounce from "lodash/debounce";
import connector from '../utils/axiosConfig';
import { ErrorMessage } from '@hookform/error-message';

interface CreateAccountRequest {
  username?: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  emailAddress?: string;
  hashedPassword?: string;
  // verificationCode?: string;
  confirmPassword?: string;
}

interface CreateAccountResponse {
  name: string;
  type: "admin" | "member";
  sessionKey: string;
  success: boolean;
  message: string;
}

const CreateAccount: React.FC = () => {
  const { register, handleSubmit, watch, getValues, setValue, formState: { errors } } = useForm({criteriaMode: "all"});
  const [passwordsDontMatch, setPasswordsDontMatch] = useState<boolean>(false);
  const [usernameTaken, setUsernameTaken] = useState<boolean>(false);
  const [emailTaken, setEmailTaken] = useState<boolean>(false);
  const [passwordVisible, setPasswordVisible] = useState<boolean>(false);
  const navigate = useNavigate();

  const nameRegEx = new RegExp(/^([^0-9]*)$/);
  const phoneRegEx = new RegExp(/^([^A-Z^a-z]{1,14})$/);
  const emailRegEx = new RegExp(/^[\w-\.\+]+@([\w-]+\.)+[\w-]{2,4}$/);
  
  
  

  const onSubmit = async (data: CreateAccountRequest) => {
    
    setUsernameTaken(false);
    setEmailTaken(false);
    
    if (data.hashedPassword != data.confirmPassword) {
      setPasswordsDontMatch(true);
      return;
    }
    let hashedPassword = sha512(data.hashedPassword!);
    data.hashedPassword = hashedPassword.toString();
    data.confirmPassword = undefined;
    await connector.post('new-member', data, {withCredentials: false})
    .then((result) => {
      if (result) {
        if (result.status == 200) {
          navigate("/login");
        }
      }
    }).catch((error) => {
      if (error.response.status == 401) {
        if (error.response.data.message.includes("e-mail")) {
          setEmailTaken(true);
        } else if (error.response.data.message.includes("username")) {
          setUsernameTaken(true);
          console.log(error);
        }
      } else {
        console.log(error);
      }
    });
  }

  const checkPasswords = (e: any) => {
    handlePasswordComparison();
  };

  const handlePasswordComparison = useCallback(
    debounce(() => {
      let passwordOne = getValues("hashedPassword");
      let passwordTwo = getValues("confirmPassword");
      if (passwordOne != "" && passwordTwo != "") {
        setPasswordsDontMatch(passwordOne != passwordTwo);
      }
    }, 500),
    []
  );

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  }


  return (
    <>
      <GuardNavbar/>
      <Form className="createAccountForm" onSubmit={handleSubmit(onSubmit)}>
        <Container className="mainContainer">
          <h2 className="centerHeader">Create account</h2>
          <Row>
            <Col>
              <Form.Label>First name</Form.Label>
              <Form.Control type="text" maxLength={36} placeholder="" 
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
            <Col>
              <Form.Label>Last name</Form.Label>
              <Form.Control type="text" maxLength={36} placeholder="" 
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
            <Form.Label>Phone number</Form.Label>
            <Form.Control 
              type="text" placeholder="" 
              {...register("phoneNumber",{
                required: "Required", 
                pattern: {
                  value: phoneRegEx ,
                  message: "Invalid Phone Number"
                },
                })}>
            </Form.Control>
            <ErrorMessage 
                errors={errors} 
                name="phoneNumber" 
                render={({ messages }) =>
                messages &&
                Object.entries(messages).map(([type, message]) => (
                  <p className="redText" key={type}>{message}</p>
                ))
              }/>
            </Col>
          </Row>
          
          {/* {errors.phoneNumber && 'Hello'} */}
          <Row>
            <Col>
              <Form.Label>Username</Form.Label>
              <Form.Control type="text" placeholder="" maxLength={36} {...register("username", {
                required: "Required"
                })}>
                </Form.Control>
                <ErrorMessage 
                errors={errors} 
                name="username" 
                render={({ messages }) =>
                messages &&
                Object.entries(messages).map(([type, message]) => (
                  <p className="redText" key={type}>{message}</p>
                ))
              }/>
              {usernameTaken ? <p className="redText">This username is taken. Please select another one.</p> : <></>}
            </Col>
          </Row>
          <Row>
            <Col>
            <Form.Label>E-mail address</Form.Label>
            <Form.Control type="text" placeholder="" maxLength={36} 
            {...register("emailAddress", {
              required: "Required", 
              pattern: {
                value: emailRegEx ,
                message: "Invalid email address"
              },
              })}>   
              </Form.Control>
              <ErrorMessage 
                errors={errors} 
                name="emailAddress" 
                render={({ messages }) =>
                messages &&
                Object.entries(messages).map(([type, message]) => (
                  <p className="redText" key={type}>{message}</p>
                ))
              }/>
              {emailTaken ? <p className="redText">This email address is taken. Do you want to <Link to="/forgot-password">reset your password?</Link></p> : <></>}
            </Col>
          </Row>
          <Row>
            <Col>
              <Form.Label>Password</Form.Label>
              
              <div style={{display: "flex", alignItems: "center"}}>
                <Form.Control type={passwordVisible ? "text" : "password"} placeholder="Password" maxLength={36} 
                {...register("hashedPassword",{required: "Required"})} onBlur={checkPasswords} style={{flex: "1", marginRight: "8px"}}></Form.Control>
                <FontAwesomeIcon icon={passwordVisible ? faEyeSlash : faEye} className="icon passwordToggle" onClick={togglePasswordVisibility} style={{cursor: "pointer"}}/>
              </div>
                <ErrorMessage 
                errors={errors} 
                name="hashedPassword" 
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
              <Form.Label>Confirm Password</Form.Label>

              <div style={{display: "flex", alignItems: "center"}}>
                <Form.Control type={passwordVisible ? "text" : "password"} placeholder=" Confirm Password" maxLength={36} 
                {...register("confirmPassword",{required: "Required"})} onBlur={checkPasswords} style={{flex: "1", marginRight: "8px"}}></Form.Control>
                <FontAwesomeIcon icon={passwordVisible ? faEyeSlash : faEye} className="icon passwordToggle" onClick={togglePasswordVisibility} style={{cursor: "pointer"}}/>
              </div>

              <ErrorMessage 
                errors={errors} 
                name="confirmPassword" 
                render={({ messages }) =>
                messages &&
                Object.entries(messages).map(([type, message]) => (
                  <p className="redText" key={type}>{message}</p>
                ))
              }/>
                {passwordsDontMatch ? <p className="redText">Passwords don't match</p> : <></>}

            </Col>
          </Row>

          
          {/* <Form.Label>Verification code</Form.Label>
          <Form.Control type="text" placeholder="" maxLength={6} {...register("verificationCode",{
                required: "Required"
                })}>
          </Form.Control>
          <ErrorMessage 
                errors={errors} 
                name="verificationCode" 
                render={({ messages }) =>
                messages &&
                Object.entries(messages).map(([type, message]) => (
                  <p className="redText" key={type}>{message}</p>
                ))
              }/> */}

          <div className="centerRow">
            <Button className="blueButton center" type="submit" disabled={passwordsDontMatch} >Create account</Button>
          </div>
          <div className="centerRow">
            or
          </div>
          <div className="centerRow">
            <Link to="/login">Already have an account? Sign in</Link>
          </div>
        </Container>
      </Form>
    </>
  );
}

export default CreateAccount;
