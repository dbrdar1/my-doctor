import React from 'react';
import '../assets/css/dugme.css'

const DugmePrijava = (props) => {

    return(
        <button disabled={props.disabled} className={props.klasa} onClick={props.onClick}>{props.tekst}</button>
    );

}

export default DugmePrijava;