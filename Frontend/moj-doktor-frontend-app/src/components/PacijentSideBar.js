import React, { useState } from 'react';
import 'antd/dist/antd.css';
import '../assets/css/meni.css'

import {
    IdcardOutlined,
    LayoutOutlined,
    CalendarOutlined,
    TeamOutlined,
    CommentOutlined,
    UserOutlined,
    SearchOutlined
} from '@ant-design/icons';

import { Layout, Menu } from 'antd';
import { NavLink } from "react-router-dom";

const { Sider } = Layout;
const { SubMenu } = Menu;

const PacijentSideBar = (props) => {
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
                            <NavLink to="/pacijent/profil" className="nav-text">
                                Moj profil
                            </NavLink>
                            </Menu.Item>
                            <Menu.Item key="uredjivanje profila">
                            <NavLink to="/pacijent/profil/uredi" className="nav-text">
                                Uređivanje profila
                            </NavLink>
                            </Menu.Item>
                        </SubMenu>
                        
                        <Menu.Item key="pocetna" icon={<LayoutOutlined />}>
                            <NavLink to="/pacijent/naslovna" className="nav-text">
                                Početna
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="karton" icon={<IdcardOutlined />} >
                            <NavLink to="/pacijent/karton/pregled" className="nav-text">
                                Moj karton
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="termini" icon={<CalendarOutlined />} >
                            <NavLink to="/pacijent/termini" className="nav-text">
                               Moji termini
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="doktori" icon={<TeamOutlined />} >
                            <NavLink to="/moji-doktori" className="nav-text">
                                Moji doktori
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="svi-doktori" icon={<SearchOutlined />} >
                            <NavLink to="/pacijent/svi-doktori" className="nav-text">
                                Pronađi doktora
                            </NavLink>
                        </Menu.Item> 
                        <Menu.Item key="razgovori" icon={<CommentOutlined />} >
                            <NavLink to="/pacijent/razgovori" className="nav-text">
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

export default PacijentSideBar;   