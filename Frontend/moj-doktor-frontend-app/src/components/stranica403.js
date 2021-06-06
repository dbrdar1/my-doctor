import React from 'react';
import 'antd/dist/antd.css';
import '../index.css';
import { Result, Button } from 'antd';
import { useHistory } from 'react-router';

const Stranica403 = () => {
  const history = useHistory();
  return (
    <Result
      status="403"
      title="403"
      subTitle="Greška pri autorizaciji. Zabranjen pristup stranici."
      extra={<Button type="default" onClick={() => {
        history.goBack();
      }}>Nazad</Button>}
    />);
}

export default Stranica403;