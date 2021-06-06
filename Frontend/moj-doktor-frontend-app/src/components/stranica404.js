import React from 'react';
import 'antd/dist/antd.css';
import '../index.css';
import { Result, Button } from 'antd';
import { useHistory } from 'react-router';

const Stranica404 = () => {
  const history = useHistory();
  return (
    <Result
      status="404"
      title="404"
      subTitle="Stranica kojoj pokušavate pristupiti nije dostupna."
      extra={<Button type="default" onClick={() => {
        history.goBack();
      }}>Nazad</Button>}
    />);
}

export default Stranica404;