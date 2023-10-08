import React, { useEffect, useState } from 'react';
import '../styles/MusicControls.scss';
import {Button, Row, Col} from "react-bootstrap";
import connector from '../utils/axiosConfig';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPlay } from '@fortawesome/free-solid-svg-icons';

interface SampleRequest {
  thisIsAString?: string;
  thisIsABool?: boolean;
}

interface SampleResponse {
  thisIsAnInt?: number;
}

const SampleComponent: React.FC = () => {
  const [theInt, setTheInt] = useState<number>(50);
  
  const doSampleRequest = async (data: SampleRequest): Promise<SampleResponse> => {
    try {
      await connector.post('samplerequest', data)
      .then((result) => {
        setTheInt(result.data.thisIsAnInt);
      });
    } catch (e: any) {
      console.log(e);
    }
    return {};
  }

  return (
    <>
      <div>
        
      </div>
    </>
  );
}

export default SampleComponent;
