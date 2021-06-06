import React from 'react';
import PacijentSideBar from './PacijentSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import Kartoni from './Kartoni';
import '../assets/css/karton.css'
import Stranica403 from './stranica403';

const PregledKartonaPacijent = (props) => {
    console.log(props);
    if(localStorage.getItem("uloga")!=='PACIJENT') return(<Stranica403/>)
    return (
        <PacijentSideBar>
            <HeaderNaslovna stranica="Moj karton" />
            <Kartoni doktor={false}/>
            <div className="pacijent-bottom"/>
        </PacijentSideBar>
    );
}

export default PregledKartonaPacijent;