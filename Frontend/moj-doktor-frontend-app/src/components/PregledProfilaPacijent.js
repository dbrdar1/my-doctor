import React from 'react';
import PacijentSideBar from './PacijentSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import '../assets/css/profil.css'
import PregledProfila from './PregledProfila';
import Stranica403 from './stranica403';

const PregledProfilaPacijent = () => {
    if(localStorage.getItem("uloga")!=='PACIJENT') return(<Stranica403/>)
    return (
        <PacijentSideBar >
            <HeaderNaslovna stranica="Moj profil" />
            <PregledProfila />
        </PacijentSideBar>
    );
}

export default PregledProfilaPacijent;