import React, { useState, useEffect,useRef } from 'react';
import { Row, Col, Card, message, Button } from 'antd';
import 'antd/dist/antd.css';
import axios from './axios';
import useDeepCompareEffect from 'use-deep-compare-effect';
import { Area, Line } from '@ant-design/plots';
import StompClient from './StompClient';
function Home() {
  const [machieInfo, setMachieInfo] = useState({});
  const [subMachiInfo, setSubMachiInfo] = useState(false);
  const ref = useRef();
  useEffect(() => {
    // 注册ws
    StompClient.connect();

    console.log(StompClient)
    let timer
    if (!subMachiInfo) {
      setSubMachiInfo(true)
      timer = setTimeout(() => {
        // 订阅采集信息
        StompClient.subscribe('/user/topic/machine/info', (msg) => {
          // console.log(msg);
          let machie = JSON.parse(msg.body);      
          let ms =Object.assign({}, ref.current);
          // 赋值图表
          if(ref.current&&ref.current[machie.macAddress]){
            machie.charts = ref.current[machie.macAddress].charts
          }
          ms[machie.macAddress] = machie
          
          setMachieInfo(ms)
          ref.current = ms;
        });
        // 订阅图表数据
        StompClient.subscribe('/user/topic/machine/info.chart', (msg) => {
          // 写入数组,重新构建数据
          let dataInfo = JSON.parse(msg.body)
          // 获取机器信息
          let machieInfo = ref.current;
          let machie = machieInfo[dataInfo.macAddress];
          // 获取机器图表信息
          let machieCharts = machie.charts;
          if (!machieCharts) {
            machieCharts = []
          }
          // 插入数据
          dataInfo.data.forEach((m) => {
            if (machieCharts.length > 60 * 3) {
              machieCharts.shift();
            }
            machieCharts.push(m);
          })
          machie.charts = machieCharts
          // 写入机器数据
          machieInfo[machie.macAddress] = machie          
          setMachieInfo(machieInfo)
          // 保持会话
          axios.get("/")
        });
      }, 2000)
    }
    return function cleanup() {
      clearTimeout(timer)
    }

  }, [])
  let machieUi = []
  if (machieInfo) {
    Object.keys(machieInfo).map(key => {
      let value = machieInfo[key];
      let data = value.charts ? value.charts : []
      const config = {
        data,
        xField: 'date',
        yField: 'value',
        seriesField: 'country',
      };
      machieUi.push(
        <Row gutter={24}  >
          <Col xs={24} sm={24} md={6} lg={4} xl={4}>
            <Card title={value.hostName} bordered={false} key={'card' + key}>
              <Row key={'card1' + key}>
                <div> <span>cpu:&nbsp;&nbsp;&nbsp;{value.cpu.toFixed(2)}%</span>          </div>
              </Row>
              <Row key={'card2' + key}>
                <div><span>内存:&nbsp;&nbsp;&nbsp;{value.userMemory}GB/{value.totalMemory}GB</span></div>
              </Row>
              <Row key={'card3' + key}>
                <div><span>采集时间:&nbsp;&nbsp;&nbsp;{value.time}</span></div>
              </Row>

              <Row key={'card4' + key}  >
                <div style={{ display: value.shutDown ? '' : 'none' }}><span>已关机</span></div>
              </Row>
              <Row key={'card5' + key}>
                <Button onClick={() => {
                  StompClient.sendMsg("/user/sendUser/shutDown", {}, JSON.stringify({ "macAddress": key }))
                }} type='primary' style={{ width: '100%' }}>关机</Button>
              </Row>
            </Card>
          </Col>
          <Col xs={24} sm={24} md={18} lg={20} xl={20}>
            <Line key={'area' + key} {...config} />
          </Col>
        </Row>
      )
    });

  }
  return (
    <div className="site-card-wrapper" key={'site-card-wrapper'}>
      <Row gutter={16} justify='end'>
        <Col xs={24} sm={10} md={10} lg={6} xl={4}>
          <span>如果你进来了代表你登录成功了</span><a href="/logout/cas">登出</a>
        </Col>

      </Row>
      {machieUi}
    </div>
  )
}
export default Home;
