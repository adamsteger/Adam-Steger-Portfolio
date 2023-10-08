import React, { useEffect, useState } from 'react';
//import {map} from 'rxjs/add/operator/map';
//import { users } from "./Users";
import { Button, Modal } from "react-bootstrap";

import './Search.scss';
import "../styles/TableModal.scss"
import PassComponent from "../components/PassComponent.tsx";
import connector from '../utils/axiosConfig';
import Cookies from 'js-cookie';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { toast } from 'react-toastify';


const Table = ({data}, {isOwner}) => {
    const [show, setShow] = useState(false);
    const [passList, setPassList] = useState([]);
    const [render, rerender] = useState(false);

    const renderPassList = (passList) => {
        var list = [];
        passList.forEach((item) => {
          list.push(<PassComponent
            firstName={item.firstName}
            lastName={item.lastName}
            email={item.email}
            expirationDate={item.expirationDate}
            usesLeft={item.usesLeft}
            usesTotal={item.usesTotal}
            usageBased={item.usageBased}
            passID={item.passID}
            revokePassFunc={revokePass} />);
        });
        return <>{list}</>
    }

    const loadPasses = async (passListReq) => {
        try {
          await connector.post('load-passes-admin', passListReq)
            .then((result) => {
              setPassList(result.data.passList);
            });
        } catch (e) {
          if (e.message.includes("401")) {
            // Not logged in or not an admin
          }
        }
        return { passList: [] };
    }

    const revokePass = async (passID) => {
      const tempReq = {
        passID: passID
      };
      try {
        await connector.post('revoke-pass', tempReq)
          .then((result) => {
            if (result.status == 200) {
              let newList = [];
              for (let i = 0; i < passList.length; i++) {
                if (passList[i].passID == passID) {
                  continue;
                }
                newList[i] = passList[i];
              }
              setPassList(newList);
            }
          });
      } catch (e) {
        console.log(e);
      }
      return {};
    }

    const promoteOrDemoteUser = async (item, doPromotion) => {
      const newType = (doPromotion) ? "ADMIN" : "USER";
      console.log(item);
      const tempReq = {
        userID: item.id,
        type: newType
      };
      await connector.post('edit-user-type', tempReq)
        .then((result) => {
          item.member_type = newType;
          rerender(!render);
        }).catch((e) => {
          toast.error("Encountered error when " + ((doPromotion) ? "promoting" : "demoting") + " user.");
        });
      return {};
  }

    const getPromoteButton = (isOwner, item) => {
      if (!isOwner) { return <></>}
      if (item.member_type.toUpperCase() === "USER") {
        return <FontAwesomeIcon icon={faArrowUp} className="promoteUserIcon" onClick={() => promoteOrDemoteUser(item, true)}/>;
      }
      return <></>;
    }

    const getDemoteButton = (isOwner, item) => {
      if (!isOwner) { return <></>}
      if (item.member_type.toUpperCase() !== "ADMIN") {
        return <></>;
      }
      return <FontAwesomeIcon icon={faArrowDown} className="demoteUserIcon" onClick={() => promoteOrDemoteUser(item, false)}/>
    }

    return (
        <>
            <table>
                <tbody>
                    <tr>
                        <th>View passes</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Username</th>
                        <th>Type</th>
                    </tr>
                    {data.list.map((item) => (
                        <tr key={item.id}>
                            <td>
                                <Button className="theme-btn" onClick={() => {
                                    loadPasses({sessionKey: Cookies.get('auth'), userID: item.id});
                                    setShow(true);
                                }}>Passes</Button></td>
                            <td>{item.first_name}</td>
                            <td>{item.last_name}</td>
                            <td>{item.username}</td>
                            <td>
                              {getPromoteButton(data.isOwner, item)}
                              {item.member_type}
                              {getDemoteButton(data.isOwner, item)}
                              </td>
                            <td>
                            {/* <div className="button">
                                <Button>Remove</Button>
                            </div> */}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <Modal className="adminEditModal" show={show} onHide={() => {setShow(false)}}>
                <Modal.Header closeButton>
                    <Modal.Title>User's passes</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {renderPassList(passList)}
                </Modal.Body>
                <Modal.Footer>
                <Button variant="secondary" onClick={() => {setShow(false)}}>
                    Close
                </Button>
                <Button className="theme-btn" onClick={() => {setShow(false)}}>
                    Save Changes
                </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default Table;