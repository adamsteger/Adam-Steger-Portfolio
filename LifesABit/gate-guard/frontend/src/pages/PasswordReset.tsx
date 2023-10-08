import React, { useCallback } from 'react';
import {useState, useEffect} from 'react';
import { useForm } from "react-hook-form";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSpinner } from '@fortawesome/free-solid-svg-icons';
import connector from '../utils/axiosConfig';
import {Button, Form} from "react-bootstrap";
import "../styles/PasswordReset.scss";
import { toast } from 'react-toastify';
import sha512 from 'crypto-js/sha512';
import { ErrorMessage } from '@hookform/error-message';
import debounce from 'lodash/debounce';
import { Link } from 'react-router-dom';
//import Table from "./Table";

interface MatchAccountRequest {
  resetID?: string;
}

interface MatchAccountResponse {
  successful: boolean;
}

interface ResetPasswordRequest {
  resetID?: String
  newHashedPassword?: string;
  confirmPassword?: string;
}

interface ResetPasswordResponse {
  successful: boolean;
}

const PasswordReset: React.FC = () => {
  const { register, handleSubmit, watch, getValues, setValue, formState: { errors } } = useForm({criteriaMode: "all"});
  const [passwordsDontMatch, setPasswordsDontMatch] = useState<boolean>(false);
  const [loading, setLoading] = useState<boolean>(true);
  const [isValid, setIsValid] = useState<boolean>(false);
  const [successfullyReset, setSuccessfullyReset] = useState<boolean>();

  let urlName = window.location.href;
  //https://www.gate-guard.com/reset-password?resetID=10aba4c3-2e72-4cd4-82ae-4975c5c3002b

  let startIndex = urlName.indexOf('=');
  let resetID1 = urlName.substring(startIndex + 1);
  
  const matchAccountFunc = async (data: MatchAccountRequest) => {
    try {
      await connector.post('match-account', data, {withCredentials: false})
      .then((result) => {
        setIsValid(result.data.success);
        setLoading(false);
      });
    } catch (e: any) {
        console.log(e);
    }
  }

  const resetPassFunc = async (data: ResetPasswordRequest) => {
    let hashedPassword = sha512(data.newHashedPassword!);
    data.newHashedPassword = hashedPassword.toString();
    data.confirmPassword = undefined;
    data.resetID = resetID1;
    try {
      await connector.post('reset-password', data, {withCredentials: false})
      .then((result) => {
        setSuccessfullyReset(result.data.success);
        if (!result.data.success) {
          toast.error("There was an error resetting your password. Please try again later.");
        }
      });
    } catch (e: any) {
        console.log(e);
        toast.error("There was an error resetting your password. Please try again later.");
    }
  }

  const checkPasswords = (e: any) => {
    handlePasswordComparison();
  };

  const handlePasswordComparison = useCallback(
    debounce(() => {
      let passwordOne = getValues("newHashedPassword");
      let passwordTwo = getValues("confirmPassword");
      if (passwordOne != "" && passwordTwo != "") {
        setPasswordsDontMatch(passwordOne != passwordTwo);
      }
    }, 500),
    []
  );

  useEffect(() => {
    if (startIndex != -1) {
      matchAccountFunc({resetID: resetID1});
    }
  }, []);
 
  return (
    <div className={`resetPassContainerDiv ${loading ? "background-gray" : ""}`}>
      {successfullyReset ? 
      <>
        <h1>Success!</h1>
        <p>Your password has been reset. You can <Link to="/login">log in here.</Link></p>
      </> :
      <>
        {loading ? 
        <div className="loadingDiv">
          <h1>Loading</h1>
          <FontAwesomeIcon className="spinner" icon={faSpinner}/>
        </div> : (isValid ? <h1>Reset password</h1> : <h1>Invalid URL</h1>)}
        {!loading && (isValid ? 
        <>
          <Form className="resetPasswordForm" onSubmit={handleSubmit(resetPassFunc)}>
            <Form.Label>Password</Form.Label>
            <Form.Control type="password" placeholder="" maxLength={36} 
            {...register("newHashedPassword",{
              required: "Required"
              })} onBlur={checkPasswords}></Form.Control>
              <ErrorMessage 
              errors={errors} 
              name="hashedPassword" 
              render={({ messages }) =>
                messages &&
                Object.entries(messages).map(([type, message]) => (
                  <p className="redText" key={type}>{message}</p>
                ))
            }/>
            <Form.Label className="mt-20">Confirm Password</Form.Label>
                <Form.Control type="password" placeholder="" maxLength={36} 
                {...register("confirmPassword",{
                  required: "Required"
                  })} onBlur={checkPasswords}></Form.Control>
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
            <Button type="submit"  className="mt-20 theme-btn">Submit</Button>
          </Form>
        </> : 
        <>
          <h2>Sorry, this URL is invalid</h2>
          <p>Please check the email and try again later.</p>
        </>)}
      </>
      }
    </div>    
  );
  
}

export default PasswordReset;