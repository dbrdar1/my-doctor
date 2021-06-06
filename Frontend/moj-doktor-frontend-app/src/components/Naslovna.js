import React from 'react';
import DoktorSideBar from './DoktorSideBar';
import PacijentSideBar from './PacijentSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import KarticaNaslovna from './KarticaNaslovna';
import '../assets/css/naslovna.css'

const Naslovna = (props) => {
    return (
        (props.doktor &&
        <DoktorSideBar>
            <HeaderNaslovna stranica="Početna" />
            <div className="naslovna-sadrzaj">
                <div className="naslovna-grid">
                    {props.kartice.map((kartica) => (
                        <KarticaNaslovna naziv={kartica.naziv} ikona={kartica.ikona} opis={kartica.opis} onClick={kartica.onClick}/>
                    ))}
                </div>
            </div>
        </DoktorSideBar>)

        ||

        (!props.doktor &&
            <PacijentSideBar>
                <HeaderNaslovna stranica="Početna" />
                <div className="naslovna-sadrzaj">
                    <div className="naslovna-grid">
                        {props.kartice.map((kartica) => (
                            <KarticaNaslovna naziv={kartica.naziv} ikona={kartica.ikona} opis={kartica.opis} onClick={kartica.onClick} />
                        ))}
                    </div>
                </div>
            </PacijentSideBar>)
    );
}

export default Naslovna;