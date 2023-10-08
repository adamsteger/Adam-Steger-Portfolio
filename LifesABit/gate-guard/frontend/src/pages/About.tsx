import React from 'react';
import '../styles/Homepage.scss';
import SampleComponent from "../components/SampleComponent";
import GuardNavbar from "../components/GuardNavbar";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import gate_guard_logo from "../resources/gate_guard.png";
import '../styles/About.scss';


const About: React.FC = (): JSX.Element => {

  return (
    <>
      <GuardNavbar/>
      <div className="contentContainer">
        <h2>Welcome to Gate Guard!</h2>
        <div>
          <p>This website was created as a Capstone Computing project for University of South Carolina CSCE490/492.</p>
          <p> Completed by Adam Steger, Chris Loftis, Erin Kremer, Henry Kiechlin, and Vaughn Eugenio.</p>
          <p>Our GitHub repository can be found <a className="accentLink" href="https://github.com/SCCapstone/LifesABit">here</a>.</p>

          <img src={gate_guard_logo} id="gate-guard-logo" alt="Gate guard logo" style={{display: 'block', margin: 'auto', width: '10%'}} />
        
        </div>
      </div>
    </>
  );
}

export default About;
