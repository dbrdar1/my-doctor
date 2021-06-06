import React, { useEffect, useState } from 'react';
import DoktorSideBar from './DoktorSideBar';
import '../assets/css/dugme.css'
import '../assets/css/termin.css'
import HeaderNaslovna from './HeaderNaslovna';
import Stranica403 from './stranica403';
import Loader from './Loader';
import { useParams } from 'react-router';
import { message } from 'antd';
import PacijentSideBar from './PacijentSideBar';

const Pregled = (props) => {
    let [loading, setLoading] = useState(true);
    const { idPregleda } = useParams();
    const [pregled, setPregled] = useState({
        simptomi: "",
        fizikalniPregled: "",
        dijagnoza: "",
        tretman: "",
        komentar: ""
    })

    useEffect(() => {
        if (localStorage.getItem("uloga") !== 'DOKTOR' && localStorage.getItem("uloga") !== 'PACIJENT') return;
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/pregledi-kartoni/pregledi/' + idPregleda, { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                setPregled({
                    simptomi: res.simptomi,
                    fizikalniPregled: res.fizikalniPregled,
                    dijagnoza: res.dijagnoza,
                    tretman: res.tretman,
                    komentar: res.komentar
                })
                setLoading(false);
            })
            .catch(() => {
                message.error("Došlo je do greške pri učitavanju podataka.");

            });
    }, []);

    const sadrzaj = <>
        <div className="lijevi-inputi pregled">
            <h3>Ime pacijenta:</h3>
            <input id="ime" type="text" value={props.location.state.ime} disabled></input>
            <h3>Prezime pacijenta:</h3>
            <input id="prezime" type="text" value={props.location.state.prezime} disabled></input>
        </div>
        <div className="desni-inputi pregled">
            <h3>Datum:</h3>
            <input id="datum" type="text" value={props.location.state.datum} disabled></input>
            <h3>Vrijeme:</h3>
            <input id="vrijeme" type="text" value={props.location.state.vrijeme} disabled></input>
        </div>
        <div className="pocisti"></div>
        <div className="odvoji"></div>
        <div className="lijevi-inputi">
            <h3>Simptomi:</h3>
            <textarea rows="2" id="komentar" value={pregled.simptomi} disabled></textarea>
            <h3>Fizikalna terapija:</h3>
            <textarea rows="2" id="komentar" value={pregled.fizikalniPregled} disabled></textarea>
            <h3>Komentar:</h3>
            <textarea rows="2" id="komentar" value={pregled.komentar} disabled></textarea>
        </div>
        <div className="desni-inputi">
            <h3>Dijagnoza:</h3>
            <textarea rows="2" id="komentar" value={pregled.dijagnoza} disabled></textarea>
            <h3>Tretman:</h3>
            <textarea rows="2" id="komentar" value={pregled.tretman} disabled></textarea>
        </div>
        <div className="pocisti"></div>
    </>


    if (localStorage.getItem("uloga") !== 'DOKTOR' && localStorage.getItem("uloga") !== 'PACIJENT') return (<Stranica403 />);
    return (
        <>
            {localStorage.getItem("uloga") === "DOKTOR" ?
                <DoktorSideBar>
                    <HeaderNaslovna stranica="Novi pregled" />
                    <div className="kartica-novi-termin">
                        {loading ? <Loader /> :
                            sadrzaj
                        }
                    </div>
                </DoktorSideBar>
                :
                <PacijentSideBar>
                    <HeaderNaslovna stranica="Novi pregled" />
                    <div className="kartica-novi-termin">
                        {loading ? <Loader /> :
                            sadrzaj
                        }
                    </div>
                </PacijentSideBar>
            }
        </>
    );
}

export default Pregled;