import React from 'react';
import {useState, useEffect} from 'react';
import { useForm } from "react-hook-form";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import gate_guard_logo from "../resources/gate_guard.png";
import gateGuardLogoWithText from '../resources/gate_guard_with_text.png';
import { faQrcode, faSpinner } from '@fortawesome/free-solid-svg-icons';
import connector from '../utils/axiosConfig';
import {Button, Modal} from "react-bootstrap";
import "../styles/usePasses.scss";
import { ToastContainer, toast } from 'react-toastify';
import { Link } from 'react-router-dom';
import { QRCode } from 'react-qrcode-logo';
//import Table from "./Table";

interface VerifyPassRequest {
  passID?: string;
}

interface VerifyPassResponse {
  isValid?: boolean;
  usageBased?: boolean;
  expirationDate?: number;
  usesLeft?: number;
  usesTotal?: number;
  message?: string;
}

interface UsePassRequest extends VerifyPassRequest {}

const UsePass: React.FC = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const [expirationDate, setExpirationDate] = useState<number | undefined>();
  const [isValid, setIsValid] = useState<boolean>(false);
  const [usageBased, setUsageBased] = useState<boolean>();
  const [usesLeft, setUsesLeft] = useState<number>();
  const [usesTotal, setUsesTotal] = useState<number>();
  const [message, setMessage] = useState<string>();
  const [showAboutPassModal, setShowAboutPassModal] = useState<boolean>(false);
  const [showQRModal, setShowQRModal] = useState<boolean>(false);

  let urlName = window.location.href;
  //https://www.gate-guard.com/use-pass?passID=10289391829

  let startIndex = urlName.indexOf('=');
  let passID1 = urlName.substring(startIndex+1);
  
  const verifyPassFunc = async (data: VerifyPassRequest) => {
    try {
      await connector.post('verify-pass', data)
      .then((result) => {
        setIsValid(result.data.isValid);
        setUsageBased(result.data.usageBased);
        setMessage(result.data.message);
        if (result.data.usageBased) {
          setUsesLeft(result.data.usesLeft);
          setUsesTotal(result.data.usesTotal);
        } else {
          setExpirationDate(result.data.expirationDate);
        }
        setLoading(false);
      });
    } catch (e: any) {
      if (e.message.includes("401")) {
      } else {
        console.log(e);
      }
    }
  }

  const doUsePassFunc = async (data: UsePassRequest) => {
    try {
      await connector.post('use-pass', data)
      .then((result) => {
        setIsValid(result.data.isValid);
        setUsageBased(result.data.usageBased);
        setMessage(result.data.message);
        if (result.data.usageBased) {
          setUsesLeft(result.data.usesLeft);
          setUsesTotal(result.data.usesTotal);
        } else {
          setExpirationDate(result.data.expirationDate);
        }
        setLoading(false);
      });
    } catch (e: any) {
      if (e.message.includes("401")) {
      } else {
        console.log(e);
      }
    }
  }

  const doUseButton = () => {
    if (usageBased && usesLeft! > 0) {
      toast("Pass used!");
      doUsePassFunc({passID: passID1});
    } else if (!usageBased) {
      toast("Pass used!");
      doUsePassFunc({passID: passID1});
    }
  }

  useEffect(() => {
    if (startIndex != -1) {
      verifyPassFunc({passID: passID1});
    }
  }, []);

 
  return (
    <>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="dark"
      />
      <div className={`usePassContainerDiv ${loading ? "background-gray" : (isValid ? "background-green" : "background-red")}`}>
        <h2 className="usePassLabel">
          {loading ? <div className="loadingDiv">
            <h1>Loading</h1>
            <FontAwesomeIcon className="spinner" icon={faSpinner}/>
          </div> : (isValid ? <h1>Pass is valid</h1> : <h1>Pass is invalid/expired!</h1>)}
          {!loading && usageBased && <>
            {usesLeft} / {usesTotal} uses
          </>}
          {!loading && !usageBased && isValid && <>
            Expires: {new Date(expirationDate!).toLocaleDateString("en-US")}
          </>}

          <br/>
          {isValid && <Button variant="light" className="marginTop5" onClick={doUseButton}>Open Gate</Button>}
          <br/>
          {isValid && <Button variant="light" className="marginTop5" onClick={() => setShowQRModal(true)}><FontAwesomeIcon icon={faQrcode} className="qrCodeIcon"/></Button>}
        </h2>
      </div>
      <div className={`usePassFooter ${loading ? "background-gray" : (isValid ? "background-green" : "background-red")}`}
           onClick={() => setShowAboutPassModal(true)}>
        <span>What is a pass?</span>
        <img src={gate_guard_logo} className="gateGuardLogo" alt="Gate guard logo"/>
      </div>

      <Modal
        show={showAboutPassModal}
        onHide={() => setShowAboutPassModal(false)}
        className="aboutPassModal"
      >
        <Modal.Header closeButton>
          <Modal.Title>About passes</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <h3>What is a pass?</h3>
          <p>A <u>pass</u> is something like a PIN code or a keycard. It's a token that tells the gate who you are, so that it will open for you. Unlike a keycard, it's harder to lose. Unlike a PIN code, it's easier to create, change, or revoke.</p>
          <h3>What can I do with a pass?</h3>
          <p>With a pass, you can open a gate from your phone or computer. At selected communities where this feature is disabled, you can display a QR code that represents your pass at the gate.</p>
          <h3>What makes a pass special?</h3>
          <p>The person that creates the pass can choose an expiration date or number of uses for the pass. They can receive notifications when the pass is used, or when it's nearly out of uses / expired. They can also choose to revoke the pass, or change the restrictions on it later.</p>
        </Modal.Body>
      </Modal>

      <Modal
        show={showQRModal}
        onHide={() => setShowQRModal(false)}
        className="qrCodeModal"
      >
        <Modal.Header closeButton>
          <Modal.Title>Pass QR Code</Modal.Title>
        </Modal.Header>
        <Modal.Body className="qrCodeModalBody">
          <QRCode value={document.location.origin + "/use-pass?passID=" + passID1} 
                    fgColor="#557B8B" bgColor="#212529"
                    size={300} logoImage={gateGuardLogoWithText}
                    logoWidth={83} logoHeight={100}/>
          <h3>Scan this code at the gate</h3>
        </Modal.Body>
      </Modal>
    </>
  );
}

export default UsePass;