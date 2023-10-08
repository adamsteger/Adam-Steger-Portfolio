import React, { useEffect, useState } from 'react';
import GuardNavbar from '../components/GuardNavbar';
import { Button, Modal, Form } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { Link, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import '../styles/LogIn.scss';
import { faUser, faLock, faEye, faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import sha512 from 'crypto-js/sha512';
import { toast } from 'react-toastify';
import connector from '../utils/axiosConfig';

interface LogInRequest {
  username?: string;
  hashedPassword?: string;
}

interface LogInResponse {
  success?: boolean;
}

const LogIn: React.FC = () => {
  const { register, handleSubmit, watch, formState: { errors } } = useForm();
  const [wrongLogin, setWrongLogin] = useState<boolean>(false);
  const navigate = useNavigate();
  const [passwordVisible, setPasswordVisible] = useState<boolean>(false);


  const onSubmit = async (data: LogInRequest) => {
    setWrongLogin(false);
    let hashedPassword = sha512(data.hashedPassword!);
    data.hashedPassword = hashedPassword.toString();
    try {
      await connector.post('log-in', data)
      .then((result) => {
        if (result.status == 200 && result.data.success) {
          navigate("/mypasses");
          // Refresh the page so the websocket gets re-established
          // and binds to a user session.
          // There may be a cleaner way of doing this, but this works
          document.location.reload();
        } else {
          setWrongLogin(true);
        }
      });
    } catch (e: any) {
      setWrongLogin(true);
    }
  }

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  }

  return (
    <>
      <GuardNavbar/>
      <Form className="logInForm" onSubmit={handleSubmit(onSubmit)}>
        <Form.Label>Username</Form.Label>
        <div className="iconInInput">
          <FontAwesomeIcon icon={faUser} className="icon"/>
          <Form.Control type="text" placeholder="Username" {...register("username")}></Form.Control>
        </div>

        <Form.Label>Password</Form.Label>
        <div className="iconInInput" style={{position: 'relative'}}>
          <FontAwesomeIcon icon={faLock} className="icon"/>
          <Form.Control type={passwordVisible ? "text" : "password"} placeholder="Password" {...register("hashedPassword")}></Form.Control>
          <FontAwesomeIcon icon={passwordVisible ? faEyeSlash : faEye} className="icon passwordToggle" onClick={togglePasswordVisibility}/>
          
        </div>

        {wrongLogin && <p className="redText">Incorrect username or password.</p>}

        <div className="centerRow">
          <Button className="blueButton center" type="submit">Sign in</Button>
        </div>
        <div className="centerRow">
          <Link to="/create-account">Create new account</Link>
        </div>
        <div className="centerRow">
          <Link to="/forgot-password">Forgot password</Link>
        </div>
      </Form>
    </>
  );
}

export default LogIn;
