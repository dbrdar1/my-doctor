import React, { useState } from 'react';
import DoktorSideBar from './DoktorSideBar';
import '../assets/css/dugme.css'
import '../assets/css/termin.css'
import HeaderNaslovna from './HeaderNaslovna';
import { message } from 'antd';
import { useHistory } from 'react-router';
import Stranica403 from './stranica403';

const DodavanjeTermina = () => {
    const [termin, setTermin] = useState({ datum: null, vrijeme: '' })
    const [ime, setIme] = useState('');
    const [prezime, setPrezime] = useState('');
    const history = useHistory();
    const spasiTermin = () => {
        fetch('http://localhost:8080/korisnici?uloga=PACIJENT', { method: "get" })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                console.log(res);
                const pacijent = res.filter((el) => {
                    return el.ime === ime && el.prezime === prezime;
                });
                if (pacijent.length === 0) {
                    message.error("Ne postoji pacijent sa zadanim podacima. Provjerite unos i pokušajte ponovo.");
                    return;
                }
                const idPacijenta = pacijent[0].id;
                const headers = new Headers({
                    "Authorization": 'Bearer ' + localStorage.getItem("token"),
                    "Content-Type": "application/json"
                });
                let unos = {
                    idPacijenta,
                    idDoktora: localStorage.getItem("id"),
                    vrijeme: termin.vrijeme,
                    datum: termin.datum
                }
                fetch('http://localhost:8080/termini/dodaj-termin', { method: "POST", body: JSON.stringify(unos), headers })
                    .then((res) => {
                        return res.json();
                    })
                    .then((res) => {
                        if (res.statusCode === 400) {
                            message.error(res.message);
                            return;
                        }
                        else {
                            message.success("Uspješno ste dodali termin.", 2);
                            history.goBack();
                        }
                    })
                    .catch((err) => {
                        console.log("ttttttttttt")
                        console.log(err)
                        message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
                    });
            })
            .catch((err) => {
                message.error("Došlo je do greške prilikom pristupa podacima.");
            });
    }

    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar>
            <HeaderNaslovna stranica="Novi termin" />
            <div className="kartica-novi-termin">
                <div className="lijevi-inputi">
                    <h3>Ime pacijenta:</h3>
                    <input id="ime-pacijenta" type="text" placeholder="Ime pacijenta" onChange={(e) => setIme(e.target.value)}></input>
                    <h3>Datum:</h3>
                    <input id="datum" type="date" placeholder="Datum"
                        onChange={(e) => setTermin(prev => { return { ...prev, datum: e.target.value } })}
                    ></input>
                </div>
                <div className="desni-inputi">
                    <h3>Prezime pacijenta:</h3>
                    <input id="prezime-pacijenta" type="text" placeholder="Prezime pacijenta" onChange={(e) => setPrezime(e.target.value)}></input>
                    <h3>Vrijeme:</h3>
                    <input id="vrijeme" type="text" placeholder="Vrijeme"
                        onChange={(e) => setTermin(prev => { return { ...prev, vrijeme: e.target.value } })}
                    ></input>
                </div>
                <div className="pocisti"></div> <br />
                <button className="uredi-cv" onClick={spasiTermin}>Spasi</button>
            </div>
        </DoktorSideBar>
    );
}

export default DodavanjeTermina;