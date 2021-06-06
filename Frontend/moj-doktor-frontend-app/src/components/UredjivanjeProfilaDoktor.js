import React from 'react';
import DoktorSideBar from './DoktorSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import UredjivanjeProfila from './UredjivanjeProfila'
import '../assets/css/profil.css'
import '../assets/css/dugme.css'
import Stranica403 from './stranica403';

const UredjivanjeProfilaDoktor = () => {
    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar >
            <HeaderNaslovna stranica="Moj profil" />
            <UredjivanjeProfila />
        </DoktorSideBar>
    );
}

export default UredjivanjeProfilaDoktor;