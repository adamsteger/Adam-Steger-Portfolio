import React, {useState, useEffect} from 'react';
import ReactDOM from 'react-dom';
import '../styles/GuardNavbar.scss';
import {Navbar, Nav, NavDropdown, Container} from "react-bootstrap";
import gate_guard_logo from "../resources/gate_guard.png";
import { Link, NavLink, useNavigate } from 'react-router-dom';
import connector from '../utils/axiosConfig';
import { AppContext, useAppContext } from '..';
import { useTheme } from "../utils/useTheme";
import { ToastContainer } from 'react-toastify';
import useDarkMode from 'use-dark-mode';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUser } from '@fortawesome/free-solid-svg-icons';

interface UserInfoRequest {
  
}

interface UserInfoResponse {
  firstName?: string;
  isAdmin?: boolean;
}

interface SignOutRequest {
  
}

interface SignOutResponse {
  success?: boolean;
}

function GuardNavbar() {

  const [greetingName, setGreetingName] = useState<string>("");
  const [isAdmin, setIsAdmin] = useState<boolean>(false);
  const [loggedIn, setLoggedIn] = useState<boolean>(false);
  const navigate = useNavigate();
  const appContext = useAppContext();
  const pageTheme = useTheme();

  const getUserInfo = async () => {
    try {
      await connector.post('user-info', {})
      .then((result) => {
        if (result.status == 200) {
          setGreetingName(result.data.firstName);
          setIsAdmin(result.data.isAdmin);
          setLoggedIn(true);
          appContext.isAdmin = result.data.isAdmin;
        }
      });
    } catch (e: any) {
      if (e?.message.includes("401")) {
        setGreetingName("");
        setLoggedIn(false);
        setIsAdmin(false);
      } else if (e) {
        console.log(e);
      }
    }
  }

  const logOutFunc = async () => {
    try {
      await connector.post('log-out', {})
      .then((result) => {
        if (result.status == 200) {
          navigate("/login");
          setGreetingName("");
        }
      });
    } catch (e: any) {
      if (e.message.includes("401")) {
        setGreetingName("");
      } else if (e) {
        console.log(e);
      }
    }
  }

  useEffect(() => {
    document.title = "Gate Guard";
    getUserInfo();
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
        theme={(useDarkMode()?.value) ? "dark" : "light"}
      />
      <Navbar bg="dark" expand="lg" variant="dark">
        <Container id="nav-container">
          <Navbar.Brand id="nav-bar">
            <Link to="/" className="noTextDeco">
              <img src={gate_guard_logo} id="gate-guard-logo" alt="Gate guard logo"/>
              Gate Guard
            </Link>
          </Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse className="justify-content-end">
            <Nav className="me-auto">
              <Nav.Link as={NavLink} to="/" className="noTextDeco">Home</Nav.Link>
              <Nav.Link as={NavLink} to="/about" className="noTextDeco">About</Nav.Link>
              {/* <Nav.Link as={NavLink} to="/updates" className="noTextDeco">Updates</Nav.Link> */}
              {loggedIn && 
                <Nav.Link as={NavLink} to="/mypasses" className="noTextDeco">MyPasses</Nav.Link>
              }
              {loggedIn && 
                <Nav.Link as={NavLink} to="/notifications" className="noTextDeco">Notifications</Nav.Link>
              }
              {loggedIn && isAdmin &&
                <Nav.Link as={NavLink} to="/settings" className="noTextDeco">Settings</Nav.Link>
              }
              {/* <NavDropdown title="Programs" id="basic-nav-dropdown">
              <NavDropdown.Item href="#action/3.1">Action</NavDropdown.Item>
              <NavDropdown.Item href="#action/3.2">Another action</NavDropdown.Item>
              <NavDropdown.Item href="#action/3.3">Something</NavDropdown.Item>
              <NavDropdown.Divider />
              <NavDropdown.Item href="#action/3.4">Separated link</NavDropdown.Item>
            </NavDropdown> */}
            <NavDropdown title={<FontAwesomeIcon icon={faUser} className="navbarProfileIcon"/>} id="user-dropdown">
              {loggedIn && <NavDropdown.Item className="navGreetings">{greetingName && <Nav.Link className="navGreetings">Hello, {greetingName}</Nav.Link>}</NavDropdown.Item>}
                {loggedIn &&
                  <NavDropdown.Item className="darkDropdownItem">
                    <Nav.Link as={NavLink} to="/settings" className="noTextDeco">My Settings</Nav.Link>
                  </NavDropdown.Item>
                }
                {loggedIn ?
                  <NavDropdown.Item className="darkDropdownItem">
                    <Nav.Link as={NavLink} to="/login" className="noTextDeco" onClick={logOutFunc}>Log Out</Nav.Link>
                  </NavDropdown.Item>
                  :
                  <NavDropdown.Item className="darkDropdownItem">
                    <Nav.Link as={NavLink} to="/login" className="noTextDeco">Log In</Nav.Link>
                  </NavDropdown.Item>
                }
            </NavDropdown>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
}

export default GuardNavbar;
