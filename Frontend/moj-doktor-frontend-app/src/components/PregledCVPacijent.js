import React from 'react';
import PregledCV from './PregledCV';
import '../assets/css/dugme.css'
import HeaderNaslovna from './HeaderNaslovna';
import PacijentSideBar from './PacijentSideBar';
import Stranica403 from './stranica403';


const PregledCVPacijent = (props) => {
    if(localStorage.getItem("uloga")!=='PACIJENT') return(<Stranica403/>)
    return(
        <PacijentSideBar >
            <HeaderNaslovna stranica="CV doktora"/>
            <PregledCV doktor = {false} idDoktora = {props.location.state.idDoktora}/>
        </PacijentSideBar>
    );
}

export default PregledCVPacijent;