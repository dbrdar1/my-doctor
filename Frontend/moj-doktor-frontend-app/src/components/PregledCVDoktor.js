import React from 'react';
import DoktorSideBar from'./DoktorSideBar';
import PregledCV from './PregledCV';
import '../assets/css/dugme.css'
import HeaderNaslovna from './HeaderNaslovna';
import Stranica403 from './stranica403';

const PregledCVDoktor = () => {
    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return(
        <DoktorSideBar >
            <HeaderNaslovna stranica="Moj CV"/>
            <PregledCV doktor = {true}/>
        </DoktorSideBar>
    );
}

export default PregledCVDoktor;