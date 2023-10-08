import React, { useEffect, useState } from 'react';
import '../styles/PassComponentStyles.scss';
import { faPencilAlt, faTrashCan, faShare, faArrowsRotate } from '@fortawesome/free-solid-svg-icons';
import Cookies from "js-cookie";
import {Button, Row, Col, Modal, Form} from "react-bootstrap";
import connector from '../utils/axiosConfig';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useForm } from 'react-hook-form';
import { Link, NavLink } from 'react-router-dom';
import { toast } from 'react-toastify';
import { QRCode } from 'react-qrcode-logo';
import gateGuardLogo from '../resources/gate_guard_with_text.png';
import { ErrorMessage } from '@hookform/error-message';

export interface PassProps extends Pass {
    revokePassFunc: (passID: string) => any;
    clearPassesFunc: () => any;
    //refreshPassesFunc: (refresh: boolean) => any;
    loadPasses: Function;
    
}

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

interface CreatePassReq extends Pass {
  
}

const PassComponent = (props: PassProps,) => {
  const { register, handleSubmit, resetField, watch, formState: { errors } } = useForm();
  const [usesPass, setUsesPass] = useState<boolean>(props.usageBased!);
  const [newPassModalIsOpen, setNewPassModalIsOpen] = React.useState(false);
  const [revokePassModalIsOpen, setRevokePassModalIsOpen] = React.useState(false);
  const [sharePassModalIsOpen, setSharePassModalIsOpen] = React.useState(false);
  
  
  const nameRegEx = new RegExp(/^([A-Za-z]*)$/);
  const usesRegEx = new RegExp(/^[1-9][0-9]*$/);
  const phoneRegEx = new RegExp(/^([^A-Z^a-z]{1,14})$/);
  const emailRegEx = new RegExp(/^[\w-\.\+]+@([\w-]+\.)+[\w-]{2,4}$/);
  const dateRegEx = new RegExp(/^\d\d\d\d-\d\d-\d\d$/);


  const editPass =  async (passData: CreatePassReq) => {
    //toast.error("Anyone??");
    
    passData.passID = props.passID;
    if (typeof passData.expirationDate == "string") {
      passData.expirationDate = new Date(passData.expirationDate + "T00:00:00").getTime();
    }
    if (passData.usageBased == undefined) {
      passData.usageBased = (passData.expirationDate != undefined);
    }
    if (passData.usageBased) {
      passData.expirationDate = -1;
    } else {
      passData.usesLeft = -1;
      passData.usesTotal = -1;    
    }
    try {
      await connector.post('edit-pass', passData)
      .then((result) => {
        props.clearPassesFunc();
        //toast.error(props.passID);
        //toast.error(passData.passID);
        if (result.status == 200) {
          props.passID = passData.passID;
          props.firstName = passData.firstName;
          props.lastName = passData.lastName;
          props.usageBased = passData.usageBased;
          props.expirationDate = passData.expirationDate;
          props.usesTotal = passData.usesTotal;
          props.usesLeft = passData.usesLeft;
          props.email = passData.email;
          //props.refreshPassesFunc(true);
          //toast.error("In Pass Comp line 77");
          toast.success("Pass Updated");
        }
        // setNewPassModalIsOpen(false);
      });
    } catch (e: any) {
      //toast.error("Houston");
      console.log(e);
    }
    setNewPassModalIsOpen(false);
    //props.refreshPassesFunc(true);
    props.loadPasses();
    return {};
  }
  const handleModalClose = () => setNewPassModalIsOpen(false);

  const copyLink = async (link: string) => {
    await navigator.clipboard.writeText(link);
    toast.info("Link copied!");
  }

  const refreshPass = async (passID: string) => {
    try {
      await connector.post('refresh-pass', {passID: passID})
      .then((result) => {
        if (result.status === 200) {
          toast.success("Pass Renewed");
          props.loadPasses();
          // TODO: Make it so they dont have to refresh
          // props.usesLeft = props.usesTotal;
        } else {
          toast.error("There was an error refreshing this pass. Please try again later.");
        }
      });
    } catch (e: any) {
      console.log(e);
    }
  }

  const resendEmail = async (passID: string) => {
    try {
      await connector.post('resend-pass-email', {passID: passID})
      .then((result) => {
        if (result.status === 200) {
          toast.success(result.data.message);
        } else {
          toast.error("There was an error refreshing this pass. Please try again later.");
        }
      });
    } catch (e: any) {
      console.log(e);
    }
  }

  useEffect(() => {
    resetField("expirationDate")
    resetField("usesTotal")
    //clear expr date or uses
  }, [usesPass]);

  return (
    <>
      <hr className="dottedhr"/>
      <div className="passComponentDiv">
            <h2>
              {props.firstName + " " + props.lastName + " "}
              <a href={`mailto:${props.email}`} className="accentColorText smallerText">{props.email}</a>
            </h2>
            {/* <h2>{props.passID}</h2> */}
            <div className="expLine">
                <FontAwesomeIcon icon={faShare} onClick={() => {setSharePassModalIsOpen(true)}}/>
                <FontAwesomeIcon icon={faPencilAlt} onClick={() => setNewPassModalIsOpen(true)}/>
                <FontAwesomeIcon icon={faTrashCan} onClick={() => setRevokePassModalIsOpen(true)}/> 
                {props.usageBased ? 
                     <h5>
                        Uses Left: <span className="expDateOrUses">{props.usesLeft} / {props.usesTotal}</span>
                        {(props.usesLeft! < props.usesTotal!) && <FontAwesomeIcon className="ml-10" icon={faArrowsRotate} onClick={() => {refreshPass(props?.passID || "")}}/>}
                     </h5> : 
                     <h5>Exp: <span className={`expDateOrUses ${((props?.expirationDate || new Date().getTime() + 100) < (new Date().getTime())) ? "strikethrough" : ""}`}>{new Date(props.expirationDate!).toLocaleDateString("en-US")}</span></h5>}
            </div>
      </div>
      <Modal
        show={newPassModalIsOpen}
        onHide={handleModalClose}
        className="newPassModal"
      >
        <Form id="newPassForm" onSubmit={handleSubmit(editPass)}>
        <Modal.Header closeButton>
          <Modal.Title>Edit pass</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Row>
            <Col>
              <Form.Label>First Name</Form.Label>
              <Form.Control className="newPassInput" defaultValue={props.firstName!} type="text" maxLength={36} placeholder="" 
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
              <Form.Control className="newPassInput" defaultValue={props.lastName!} type="text" maxLength={36} placeholder="" 
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
              <Form.Control className="newPassInput" type="text" defaultValue={props.email!} placeholder="" maxLength={36} 
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
          <Form.Check label="Usage-based" type="switch" checked={usesPass} {...register("usageBased")} onChange={() => setUsesPass(!usesPass)}></Form.Check>
          {usesPass ? 
            <>
              <Form.Label>Total uses</Form.Label>
              {/* TODO: max based on admin */}
              <Form.Control className="newPassInput" type="text" defaultValue={String(props.usesTotal)} placeholder="e.g. 5" 
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
              <Form.Control className="newPassInput" 
              defaultValue={new Date(props.expirationDate!).toLocaleDateString("en-US")} type="date" 
              {...register("expirationDate",{
                required: "Required",
                // validate: (value: any) => (new Date(value) > new Date())
                validate: {
                  dateValid: inDate => {
                    console.log(inDate);
                    let temp = inDate;
                    inDate = new Date(inDate);
                    let offset = inDate.getTimezoneOffset() / 60;
                    inDate.setHours(inDate.getHours() + offset);
                    return (inDate > new Date()) && dateRegEx.test(temp);
                  } ,
                  
                }
                })}>   
              </Form.Control>
              <ErrorMessage
                    errors={errors}
                    name="expirationDate"
                    render={() => <p className='redText'>Invalid Date</p>}
                  />
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
          <Button type="submit" className="theme-btn-2">
            Update
          </Button>
        </Modal.Footer>
        </Form>
      </Modal>



      <Modal
        show={revokePassModalIsOpen}
        onHide={() => setRevokePassModalIsOpen(false)}
        className="newPassModal"
      >
        
        <Modal.Header closeButton>
          <Modal.Title>Revoke pass</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          Are you sure you would like to revoke {props.firstName} {props.lastName}'s pass
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setRevokePassModalIsOpen(false)}>
            Cancel
          </Button>
          <Button className="theme-btn" onClick={() => {props.revokePassFunc(props.passID!); setRevokePassModalIsOpen(false)}}>
            Revoke
          </Button>
        </Modal.Footer>
        
      </Modal>

      <Modal
        show={sharePassModalIsOpen}
        onHide={() => setSharePassModalIsOpen(false)}
        className="sharePassModal"
      >
        <Modal.Header closeButton>
          <Modal.Title>Share Pass</Modal.Title>
        </Modal.Header>
        <Modal.Body className="shareModalBody">
          <QRCode value={document.location.origin + "/use-pass?passID=" + props.passID} 
                  fgColor="#557B8B" bgColor="#212529"
                  size={300} logoImage={gateGuardLogo}
                  logoWidth={83} logoHeight={100}/>
          <p>Share the QR code above with {props.firstName} so they can use this pass, or send them to
            <a href={document.location.origin + "/use-pass?passID=" + props.passID} className="sharePassLink">this link.</a>
          </p>
        </Modal.Body>
        <Modal.Footer>
          <Button className="theme-btn" onClick={() => resendEmail(props.passID!)}>Resend Email</Button>
          <Button className="theme-btn" onClick={() => {copyLink(document.location.origin + "/use-pass?passID=" + props.passID)}}>Copy Link</Button>
          <Button variant="secondary" onClick={() => setSharePassModalIsOpen(false)}>
            Cancel
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}

export default PassComponent;