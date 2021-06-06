import React from 'react';
import '../assets/css/naslovna.css'

const KarticaNaslovna = (props) => {
    return (
        <div className="kartica" onClick={props.onClick}>
            <div onClick={props.onClick}>
                <img className="ikona" data-testid="ikona-kartica" src={props.ikona} alt="ikona" onClick={props.onClick}></img>
                <h2 className="naziv" onClick={props.onClick}>{props.naziv}</h2>
                <p className="opis-kartice" onClick={props.onClick}>{props.opis}</p>
            </div>
        </div>

    );
}

export default KarticaNaslovna;