import React, { useState } from 'react';
import 'antd/dist/antd.css';
import '../assets/css/meni.css'

import {
    IdcardOutlined,
    LayoutOutlined,
    CalendarOutlined,
    TeamOutlined,
    DiffOutlined,
    CommentOutlined,
    UserOutlined,
    SearchOutlined,
    FieldTimeOutlined,
} from '@ant-design/icons';

import { Layout, Menu } from 'antd';
import { NavLink } from "react-router-dom";

const { Sider } = Layout;
const { SubMenu } = Menu;

const DoktorSideBar = (props) => {
    const [collapsed, setCollapsed] = useState(false);

    const onCollapse = () => {
        setCollapsed(!collapsed);
    };

    return (
        <div className="cijela-stranica">
            <Layout style={{ minHeight: '100vh' }}>
                <Sider theme="dark" collapsible collapsed={collapsed} onCollapse={onCollapse} >
                    <div className="naslov"><h2>Moj doktor</h2></div>
                    <Menu theme="dark" defaultSelectedKeys={['1']} mode="inline">
                        <SubMenu key="korisnik" icon={<UserOutlined />} title={localStorage.getItem('ime')+' '+localStorage.getItem('prezime')}>
                            <Menu.Item key="profil">
                            <NavLink to="/doktor/profil" className="nav-text">
                                Moj profil
                            </NavLink>
                            </Menu.Item>
                            <Menu.Item key="uredjivanje profila">
                            <NavLink to="/doktor/profil/uredi" className="nav-text">
                                Uređivanje profila
                            </NavLink>
                            </Menu.Item>
                        </SubMenu>
                        <Menu.Item key="pocetna" icon={<LayoutOutlined />}>
                            <NavLink to="/doktor/naslovna">
                                Početna
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="cv" icon={<IdcardOutlined />} >
                            <NavLink to="/doktor/cv" className="nav-text">
                                Moj CV
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="termini" icon={<CalendarOutlined />} >
                            <NavLink to="/doktor/termini" className="nav-text">
                                Raspored termina
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="termin" icon={<FieldTimeOutlined />} >
                            <NavLink to="/termin/dodaj" className="nav-text">
                                Dodaj termin
                            </NavLink>
                         </Menu.Item>
                         <Menu.Item key="pregled" icon={<DiffOutlined />} >
                            <NavLink to="/pregled/dodaj" className="nav-text">
                                Dodaj pregled
                            </NavLink>
                         </Menu.Item>
                         <Menu.Item key="pacijenti" icon={<TeamOutlined />} >
                            <NavLink to="/moji-pacijenti" className="nav-text">
                                Moji pacijenti
                            </NavLink>
                        </Menu.Item>
                         <Menu.Item key="svi-pacijenti" icon={<SearchOutlined />} >
                            <NavLink to="/doktor/svi-pacijenti" className="nav-text">
                                Pronađi pacijenta
                            </NavLink>
                        </Menu.Item> 
                         <Menu.Item key="razgovori" icon={<CommentOutlined />} >
                            <NavLink to="/doktor/razgovori" className="nav-text">
                                Razgovori
                            </NavLink>
                        </Menu.Item>                        
                    </Menu>
                </Sider>
                <Layout className="site-layout">
                    {props.children}
                </Layout>
            </Layout>
        </div>
    );

}

export default DoktorSideBar;