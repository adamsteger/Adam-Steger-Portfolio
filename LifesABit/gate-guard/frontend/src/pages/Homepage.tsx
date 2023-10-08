import React, { useState } from 'react';
import '../styles/Homepage.scss';
import SampleComponent from "../components/SampleComponent";
import GuardNavbar from "../components/GuardNavbar";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars, faBell } from '@fortawesome/free-solid-svg-icons';
import { Parallax } from 'react-parallax';
import cyberLockBg from '../resources/cyberlock_bg.jpg';
import gateGuardLogo from '../resources/gate_guard_with_text.png';
import qrCodeScreenshotDark from '../resources/qr_code_screenshot.png';
import qrCodeScreenshotLight from '../resources/qr_code_screenshot_light.png';
import peopleUsingGateguardDark from '../resources/people_using_gateguard.png';
import peopleUsingGateguardLight from '../resources/people_using_gateguard_light.png';
import notificationBg from '../resources/notification_bg.jpg';
import neighborhoodBg from '../resources/neighborhood_at_night.jpg';
import notificationsScreenshotDark from '../resources/notifications_screenshot.png';
import notificationsScreenshotLight from '../resources/notifications_screenshot_light.png';
import { QRCode } from 'react-qrcode-logo';
import { Modal } from 'react-bootstrap';
import { useTheme } from '../utils/useTheme';


const Homepage: React.FC = (): JSX.Element => {
  const [showImageModal, setShowImageModal] = useState<boolean>(false);
  const theme = useTheme();

  return (
    <>
      <GuardNavbar/>
      <div>
        <div className="topDivBeforeParallax">
          <h2 className="centerAligned"><strong>Welcome to Gate Guard!</strong></h2>
          <p className="centerAligned">
            A Secure Token Authentication App
            <br/>
            for Gated Communities and Neighborhoods
          </p>
        </div>
        <Parallax blur={{ min: -10, max: 15 }} bgImage={cyberLockBg} bgImageAlt="Picture of lock" strength={200} className="parallaxSection">
          <div className="parallaxSizeContainer">
            <div className="leftParallaxFloater shadedContainer">
                <p className="parallaxHeader parallaxText">Security</p>
                <p className="parallaxText">Authenticate your identity with a secure token or QR code.</p>
                <p className="parallaxText">Community administrators can control how long passes are granted for, how many times a pass may be used, and can revoke passes from guests at any time.</p>
            </div>
            <img src={theme === "dark-mode" ? qrCodeScreenshotDark : qrCodeScreenshotLight} className="qrCodeScreenshot bottomRightFloater"/>
          </div>
        </Parallax>
        <div className="whiteBar"><FontAwesomeIcon className="whiteBarIcon" icon={faBars}/></div>
        <Parallax blur={{ min: -10, max: 15 }} bgImage={theme === "dark-mode" ? peopleUsingGateguardDark : peopleUsingGateguardLight} bgImageAlt="Picture of lock" strength={200} className="parallaxSection">
          <div className="parallaxSizeContainer">
            <div className="rightParallaxFloater shadedContainer mb-10-perc mt-10-perc">
              <p className="parallaxHeader">Easy Interface and Access Control</p>
              <p>Our seamless interface is easy for even Grandma and Grandpa to use! </p>
              <p>Save and manage tokens at will. </p>
              <p>Add or revoke passes at any time to manage your guests' access to the community. </p>
              <p>Create passes for frequent visitors, delivery drivers, sanitation workers, or anyone who may need access to your community.</p>
            </div>
          </div>
        </Parallax>
        <div className="whiteBar"><FontAwesomeIcon className="whiteBarIcon" icon={faBars}/></div>
        <Parallax blur={{ min: -5, max: 15 }} bgImage={notificationBg} bgImageAlt="Picture of devices with notifications" strength={200} className="parallaxSection">
          <div className="parallaxSizeContainer centerAllParallax">
            <div className="centerFloater shadedContainer mb-10-perc mt-10-perc">
              <p className="parallaxHeader">Never Miss Important Notifications</p>
              <div className="bellBox">
                <div className="jagged leftSideGradient"/>
                <FontAwesomeIcon icon={faBell} className="bellIcon"/>
                <div className="jagged rightSideGradient"/>
              </div>
              <p>Be notified instantly via email when a pass is used to know when your guests are arriving. </p>
              <p>Get alerted when your guests' passes are about to expire, so they never have to wait. </p>
              <div className="verticalImageDivInShaded">
                <img src={theme === "dark-mode" ? notificationsScreenshotDark : notificationsScreenshotLight} className="parallaxScreenshot" onClick={() => setShowImageModal(true)}/>
              </div>
            </div>
          </div>
        </Parallax>
        <div className="whiteBar"><FontAwesomeIcon className="whiteBarIcon" icon={faBars}/></div>
        <Parallax blur={{ min: -10, max: 15 }} bgImage={neighborhoodBg} bgImageAlt="Picture of devices with notifications" strength={200} className="parallaxSection">
          <div className="parallaxSizeContainer centerAllParallax">
            <div className="centerFloater shadedContainer mb-10-perc mt-10-perc">
              <p>Gate Guard can make your neighborhood a safer, more accessible place to live</p>
              <div className="centeredVideo">
                <iframe className="youtubeVideo" src="https://www.youtube.com/embed/Y-x0efG1seA" title="YouTube video player" frameBorder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowFullScreen></iframe>
              </div>
              <p>Check out our demo video to see just how easy Gate Guard can be</p>
            </div>
          </div>
        </Parallax>
      </div>
      <Modal
          show={showImageModal}
          onHide={() => setShowImageModal(false)}
          className="notificationsScreenshotModal"
      >
        <Modal.Header closeButton></Modal.Header>
        <Modal.Body className="notificationsScreenshotModalBody">
          <img src={theme === "dark-mode" ? notificationsScreenshotDark : notificationsScreenshotLight}/>
        </Modal.Body>
      </Modal>
    </>
  );
}

export default Homepage;
