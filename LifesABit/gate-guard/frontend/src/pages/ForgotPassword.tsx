import React from 'react';
import {useState, useEffect} from 'react';
import { useForm } from "react-hook-form";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';
import connector from '../utils/axiosConfig';
import {Button, Form} from "react-bootstrap";
import "../styles/ForgotPassword.scss";
import { toast } from 'react-toastify';
import {emailRegEx} from './MyPasses';
import GuardNavbar from '../components/GuardNavbar';

interface RequestPasswordResetRequest {
  email?: String;
}

interface RequestPasswordResetResponse {

}

const ForgotPassword: React.FC = () => {
  const { register, handleSubmit, watch, formState: { errors } } = useForm();
  const [done, setDone] = useState<boolean>();

  const requestResetFunc = async (data: RequestPasswordResetRequest) => {
    try {
      await connector.post('request-reset', data, {withCredentials: false})
      .then((result) => {
        setDone(true);
      });
    } catch (e: any) {
        console.log(e);
    }
  }

  return (
    <>
      <GuardNavbar/>
      <Form className="forgotPasswordForm" onSubmit={handleSubmit(requestResetFunc)}>
        {done ?
        <>
          <h2>Success!</h2>
          <p className="blueText ">A password reset link has been sent to the email you entered.</p>
          <p className="blueText ">Please wait a few minutes if it does not arrive, and check your spam folder.</p>
        </> : 
        <>
          <h1>Reset Password</h1>
          <Form.Label>Email</Form.Label>
          <Form.Control className="newPassInput" type="text" placeholder="" maxLength={36} 
            {...register("email",
              {
              required: "Required", 
              pattern: {
                value: emailRegEx,
                message: "Invalid email address"
              },
            })}>
          </Form.Control>
          <Button type="submit"  className="theme-btn">Submit</Button>
        </>
        }
      </Form>
    </>
  );
  
}

export default ForgotPassword;