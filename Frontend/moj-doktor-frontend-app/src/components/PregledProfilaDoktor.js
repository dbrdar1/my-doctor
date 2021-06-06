import React from 'react';
import DoktorSideBar from './DoktorSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import '../assets/css/profil.css'
import PregledProfila from './PregledProfila';
import Stranica403 from './stranica403';

const PregledProfilaDoktor = () => {
    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar >
            <HeaderNaslovna stranica="Moj profil" />
            <PregledProfila />
        </DoktorSideBar>
    );
}

export default PregledProfilaDoktor;