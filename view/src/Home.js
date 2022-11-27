import React, { useState, useEffect,useRef } from 'react';
import { Row, Col, Card, message, Button } from 'antd';
import 'antd/dist/antd.css';
import axios from './axios';
import useDeepCompareEffect from 'use-deep-compare-effect';
import { Area, Line } from '@ant-design/plots';
function Home() {
  useEffect(() => {


  }, [])
  return (
    <div className="site-card-wrapper" key={'site-card-wrapper'}>
      <Row gutter={16} justify='end'>
        <Col xs={24} sm={10} md={10} lg={6} xl={4}>
          <span>如果你进来了代表你登录成功了</span><a href="/logout/cas">登出</a>
        </Col>

      </Row>
    </div>
  )
}
export default Home;
