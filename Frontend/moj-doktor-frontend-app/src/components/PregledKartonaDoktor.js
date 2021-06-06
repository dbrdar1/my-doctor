import React from 'react';
import DoktorSideBar from './DoktorSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import Kartoni from './Kartoni';
import Stranica403 from './stranica403';

// podaci={props.location.state.podaci}

// Ovo se definiše u komponenti moji pacijenti i kad 
// doktor klikne na opciju za prikaz kartona pacijenta popuni se
// podacima iz get zahtjeva upućen na /pregledi-kartoni/kartoni/{idPacijent}
// idPacijent treba nekako dobiti? 

const PregledKartonaDoktor = (props) => {
    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar>
            <HeaderNaslovna stranica="Pregled kartona" />
            <Kartoni doktor={true} pacijent = {props.location.state.pacijentPodaci}/>
        </DoktorSideBar>
    );
}

export default PregledKartonaDoktor;