import React from 'react';
import ReactDOM from 'react-dom/client';
import './styles/index.scss';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Homepage from './pages/Homepage';
import About from './pages/About';
import NoPage from './pages/NoPage';
import Updates from './pages/Updates';
import MyPasses from './pages/MyPasses';
import Notifications from './pages/Notifications';
import LogIn from "./pages/LogIn";
import Settings from "./pages/Settings";
import Search from "./pages/Search";
import Table from "./pages/Table";
import useDarkMode from "use-dark-mode";



import CreateAccount from "./pages/CreateAccount";
import UsePass from "./pages/UsePass";
import "./styles/main.scss";
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import { WebsocketContext } from './components/WebsocketContext';
import PasswordReset from './pages/PasswordReset';
import ForgotPassword from './pages/ForgotPassword';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

export interface AppContextType {
  isAdmin: boolean;
}

export const AppContext = React.createContext({isAdmin: false} as AppContextType);

root.render(
  <React.StrictMode>
    <AppContext.Provider value={{isAdmin: false}}>
      <WebsocketContext/>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Homepage/>}/>
          <Route path="/about" element={<About/>}/>
          <Route path="updates" element={<Updates/>}/>
          <Route path="*" element={<NoPage/>}/>
          <Route path="/login" element={<LogIn/>}/>
          <Route path="/settings" element={<Settings/>}/>
          <Route path="/mypasses" element={<MyPasses/>}/>
          <Route path="/notifications" element={<Notifications/>}/>
          <Route path="/create-account" element={<CreateAccount/>}/>
          <Route path="/use-pass" element={<UsePass/>}/>
          <Route path="/reset-password" element={<PasswordReset/>}/>
          <Route path="/forgot-password" element={<ForgotPassword/>}/>
        </Routes>
      </BrowserRouter>
    </AppContext.Provider>
  </React.StrictMode>
);

export const useAppContext = () => React.useContext(AppContext);