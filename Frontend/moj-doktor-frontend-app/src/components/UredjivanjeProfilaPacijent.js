import React from 'react';
import PacijentSideBar from './PacijentSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import UredjivanjeProfila from './UredjivanjeProfila'
import '../assets/css/profil.css'
import '../assets/css/dugme.css'
import Stranica403 from './stranica403';

const UredjivanjeProfilaPacijent = () => {
    if(localStorage.getItem("uloga")!=='PACIJENT') return(<Stranica403/>)
    return (
        <PacijentSideBar >
            <HeaderNaslovna stranica="Moj profil" />
            <UredjivanjeProfila />
        </PacijentSideBar>
    );
}

export default UredjivanjeProfilaPacijent;