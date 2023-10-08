import React, { useEffect } from 'react';
import GuardNavbar from "../components/GuardNavbar";
import {useState} from "react";
import { useForm } from "react-hook-form";
import "../styles/Notifications.scss";
import connector from '../utils/axiosConfig';
import { faAdd, faArrowLeft, faArrowRight, faChevronLeft, faChevronRight, faEnvelope, faEnvelopeOpen } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button, Col, Container, Row } from 'react-bootstrap';
import { title } from 'process';
import styled from 'styled-components';
import { toast } from 'react-toastify';
import { useTable, usePagination } from 'react-table';

const NotificationType = {
  PASS_USED : "PASS_USED",
  PASS_EXPIRED : "PASS_EXPIRED",
  PASS_EXPIRES_SOON : "PASS_EXPIRES_SOON"
}

interface Notification {
  notificationID?: string;
  message?: string;
  expDate?: string;
  time?: string;
  passID?: string;
  userID?: string;
  title?: string;
  description?: string;
  timestamp: number;
  expirationDate: number;
  usesLeft: number;
  email?: string;
  usageBased: boolean | null;
  read?: boolean;
  type?: typeof NotificationType;
  ipAddress?: string;
}

interface LoadNotifsResponse {
  notificationList: Notification[];
}

interface UpdateNotifRequest {
  notifID?: string;
  read?: boolean;
}

interface UpdateNotifResponse {
  success: boolean;
  message: string;
  notif: Notification;
}

const Notifications: React.FC = () => {
  const { register, handleSubmit, watch, formState: { errors } } = useForm();
  const [notificationList, setNotificationList] = React.useState<Notification[]>([]);
  const [newPassModalIsOpen, setNewPassModalIsOpen] = React.useState(false);
  //State Variable for number of unread notifications
  const [unreadCount, setUnreadCount] = React.useState(0);
  const columns = React.useMemo(
    () => [
      {
        Header: "Title",
        accessor: "title"
      },
      {
        Header: "Email",
        accessor: "email"
      },
      {
        Header: "Uses left",
        accessor: "usesLeft"
      },
      {
        Header: "Expiration date",
        accessor: "expirationDate"
      },
      {
        Header: "Usage based",
        accessor: "usageBased"
      },
      {
        Header: "IP",
        accessor: "ipAddress"
      },
      {
        Header: "Timestamp",
        accessor: "timestamp"
      }
    ],
    []
  );
  {/* @ts-ignore */}
  const tableInstance = useTable({ columns, data: notificationList }, usePagination);
  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    rows,
    prepareRow,
    /* @ts-ignore */
    page,
    /* @ts-ignore */
    canPreviousPage,
    /* @ts-ignore */
    canNextPage,
    /* @ts-ignore */
    pageOptions,
    /* @ts-ignore */
    pageCount,
    /* @ts-ignore */
    gotoPage,
    /* @ts-ignore */
    nextPage,
    /* @ts-ignore */
    previousPage,
    /* @ts-ignore */
    setPageSize,
    /* @ts-ignore */
    state: {pageIndex, pageSize},
  } = tableInstance;

  const handleNotificationClick = (id: string) => {
    const updatedNotificationList = notificationList.map(notification => {
      if (notification.notificationID === id) {
       notification.read = !notification.read;
      }
      return notification;
    });
    setNotificationList(updatedNotificationList.sort((a, b) => {return b.timestamp - a.timestamp}));
    setUnreadCount(unreadCount - 1);
  }

  const unreadNotifications = notificationList.reduce((count, notification) => {
    return count + (notification.read ? 0 : 1);
  }, 0);

  const getDateString = (timestamp: number): string => {
    let later = new Date();
    let sooner = new Date(timestamp);
    let toPrint = sooner;
    if (sooner.getTime() > later.getTime()) {
      let temp = later;
      later = sooner;
      sooner = temp;
      toPrint = later;
    }
    const MILLISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;
    if ((later.getTime() - sooner.getTime()) < MILLISECONDS_IN_A_DAY) {
      return toPrint.toLocaleTimeString();
    } else {
      return toPrint.toLocaleDateString();
    }
  }

  const getPassInfoText = (usageBased: boolean | null, expDate: number, usesLeft: number): string => {
    if (usageBased == null) {
      return "> Pass revoked <";
    }
    return usageBased ? "Uses remaining: " + usesLeft : "Expires " + getDateString(expDate);
  }
  
  //Base notification
  const loadNotification = async (): Promise<LoadNotifsResponse> => {
    try {
        await connector.post('load-notifs', {})
        .then((result) => {
          setNotificationList(result.data.notificationList.sort((a: Notification, b: Notification) => {return b.timestamp - a.timestamp}));
        });
    } catch (e: any) {
      if (e.message.includes("401")) {
        //setNotLoggedIn(true);
        //TODO: FIX THIS
        console.log("Load Notification Error: 401");
      }
    }
    return { notificationList: [] };
  }

  const updateNotification = async(notifID: string, newStatus: boolean) => {
    let reqBody: UpdateNotifRequest = {notifID: notifID, read: !newStatus};
    try {
      await connector.post('update-notif', reqBody)
      .then((result) => {
        if (result.status !== 200) {
          toast.error("There was an error when setting this notification's status. Please try again later.");
        } else {
          
        }
      });
    } catch (e: any) {
      if (e.message.includes("401")) {
        //setNotLoggedIn(true);
        //TODO: FIX THIS
        console.log("Load Notification Error: 401");
        toast.error("There was an error when setting this notification's status. Please try again later.");
      }
    }
  }

  useEffect(() => {
    loadNotification();
   }, []);
   //<h2> Notifications <Button>Read All Messages</Button></h2>

  return (
    <>
      <GuardNavbar/>
      <div>
        <div className="notification-header-div">
          <h2 className="notificationPageHeader">Notifications</h2>
          <div className={unreadNotifications >= 1 ? "unread-count" : "no-unread-count"}>
            {unreadNotifications >= 1 ? unreadNotifications : null}
          </div>
        </div>
        <hr></hr>
      </div>
      <div className="contentDiv">

        <Container fluid className="notifContainer">
          <div className="pageCountMarker"><h3>Page {pageIndex + 1} of {pageCount}</h3></div>
          <div className="notifPageControls">
            <Button className="theme-btn" onClick={() => gotoPage(0)}><FontAwesomeIcon icon={faChevronLeft}/><FontAwesomeIcon icon={faChevronLeft}/></Button>
            <Button className="theme-btn" onClick={() => previousPage()}><FontAwesomeIcon icon={faChevronLeft}/></Button>
            <Button className="theme-btn" onClick={() => nextPage()}><FontAwesomeIcon icon={faChevronRight}/></Button>
            <Button className="theme-btn" onClick={() => gotoPage(pageCount - 1)}><FontAwesomeIcon icon={faChevronRight}/><FontAwesomeIcon icon={faChevronRight}/></Button>
          </div>
          <Row className="notifRow notifHeader">
            <Col className="notifIconDiv">
              <FontAwesomeIcon icon={faEnvelopeOpen}
                               className="notif-icon invisible"/>
            </Col>
            <Row>
              <Col lg className="notifFoldableContent notifHeaderText">
                Title
              </Col>
              <Col lg className="notifFoldableContent notifHeaderText">
                Email
              </Col>
              <Col lg className="notifFoldableContent notifHeaderText">
                Resources
              </Col>
              <Col lg className="notifFoldableContent notifHeaderText">
                IP
              </Col>
              <Col md={2} className="notifTimestampDiv notifHeaderText">
                Timestamp
              </Col>
            </Row>
            {/* <p className={(notification.expDate) ? (getExpDateNear(notification.expDate) ? "red-text" : "") : ""}>{notification.expDate}</p> */}
          </Row>
          {page.map((row: any) => {
            prepareRow(row);
            return (
              <Row className="notifRow">
                <Col className="notifIconDiv">
                  <FontAwesomeIcon icon={row.values.read ? faEnvelopeOpen : faEnvelope}
                                  className="notif-icon"
                                  onClick={(event) => {handleNotificationClick((row.values.notificationID) ? row.values.notificationID! : "");
                                  updateNotification(row.values.notificationID!, !row.values.read)}}/>
                </Col>
                <Row>
                  <Col lg className="notifFoldableContent">
                    <strong>{row.values.title}</strong>
                  </Col>
                  <Col lg className="notifFoldableContent">
                    {row.values.email ? row.values.email : "> Pass revoked <"}
                  </Col>
                  <Col lg className="notifFoldableContent">
                    {getPassInfoText(row.values.usageBased, row.values.expirationDate, row.values.usesLeft)}
                  </Col>
                  <Col lg className="notifFoldableContent">
                    {(row.values.ipAddress) ? row.values.ipAddress : "None"}
                  </Col>
                  <Col md={2} className="notifTimestampDiv">
                    {<span className="accentColor">{getDateString(row.values.timestamp!)}</span>}
                  </Col>
                </Row>
                {/* <p className={(notification.expDate) ? (getExpDateNear(notification.expDate) ? "red-text" : "") : ""}>{notification.expDate}</p> */}
              </Row>
          )})}
        </Container>
      </div>
    </>
  );
}

export default Notifications;
