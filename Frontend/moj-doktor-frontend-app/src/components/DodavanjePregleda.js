import DoktorSideBar from './DoktorSideBar';
import '../assets/css/dugme.css'
import '../assets/css/termin.css'
import HeaderNaslovna from './HeaderNaslovna';
import React, {useState} from 'react';
import { message } from 'antd';
import '../assets/css/karton.css';
import { useHistory } from 'react-router';
import Stranica403 from './stranica403';

const DodavanjePregleda = (props) => {

    const history = useHistory();
    
    const [podaci, setPodaci] = useState({
        ime: '', prezime: '', datum: null, vrijeme: '',
        simptomi: '', dijagnoza: '', fizikalniPregled: '', tretman: '', komentar: ''
    });

    const dodajPregled= () => {
        fetch('http://localhost:8080/korisnici?uloga=PACIJENT', { method: "get" })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                const pacijent = res.filter((el) => {
                    if(props.location.state == null)
                           return el.ime === podaci.ime && el.prezime === podaci.prezime
                    return el.ime === props.location.state.pacijentPodaci.ime && el.prezime === props.location.state.pacijentPodaci.prezime
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
                // ovdje get za termini pacijenta preko id pacijenta
                // kad se dobiju termini filtrirati po vremenu i datumu (podaci.vrijeme, podaci.datum)- uzeti tacno jedan termin
                // u unos staviti
                fetch('http://localhost:8080/pregledi-kartoni/termini-pacijenta/' + idPacijenta, { method: "get", headers })
                    .then((res) => {
                        return res.json();
                    })
                    .then((res) => {
                    const termin = res.filter((el) => {
                        return el.vrijemePregleda === podaci.vrijeme && el.datumPregleda.toString().substr(0, el.datumPregleda.toString().indexOf('T')) === podaci.datum;
                    });
                    
                    if (termin.length === 0) {
                        message.error("Ne postoji termin sa zadanim podacima. Provjerite unos i pokušajte ponovo.");
                        return;
                    }
                    const idTermina = termin[0].id;
                    const headers = new Headers({
                        "Authorization": 'Bearer ' + localStorage.getItem("token"),
                        "Content-Type": "application/json"
                    });
                    let unos = {
                        terminId: idTermina,
                        // citanje sa forme
                        simptomi: podaci.simptomi,
                        fizikalniPregled: podaci.fizikalniPregled,
                        dijagnoza: podaci.dijagnoza,
                        tretman: podaci.tretman,
                        komentar: podaci.komentar
                    }
                    fetch('http://localhost:8080/pregledi-kartoni/dodaj-pregled', { method: "POST", body: JSON.stringify(unos), headers})
                        .then((res) => {
                            return res.json();
                        })
                        .then((res) => {
                            if (res.statusniKod !== 200) {
                                message.error(res.poruka);
                                return;
                            }
                            else {
                                message.success("Uspješno ste dodali pregled.", 2);
                                history.goBack();
                            }
                        })
                        .catch((err) => {
                            message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
                        });
                    })
                    .catch((err) => {
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
            <HeaderNaslovna stranica="Novi pregled" />
            <div className="kartica-novi-termin">
                {(props.location.state == null &&
                <div className="lijevi-inputi pregled">
                    <h3>Ime:</h3>
                    <input id="ime" type="text" placeholder="Ime pacijenta" value = {podaci.ime} onChange={(e) => setPodaci((prev) => { return { ...prev, ime: e.target.value } })}></input>
                    <h3>Prezime:</h3>
                    <input id="prezime" type="text" placeholder="Prezime pacijenta" value = {podaci.prezime} onChange={(e) => setPodaci((prev) => { return { ...prev, prezime: e.target.value } })}></input>   
                </div>)
                ||
                (props.location.state != null &&
                <div className="lijevi-inputi pregled">
                    <h3>Ime: </h3>
                    <input id="ime" type="text" placeholder="Ime pacijenta" value = {props.location.state.pacijentPodaci.ime} disabled={true}></input>
                    <h3>Prezime: </h3>
                    <input id="prezime" type="text" placeholder="Prezime pacijenta" value = {props.location.state.pacijentPodaci.prezime} disabled={true}></input>
                    
                </div>)
                }
                <div className="desni-inputi pregled">
                    <h3>Datum:</h3>
                    <input id="datum" type="date" placeholder="Datum" value = {podaci.datum} onChange={(e) => setPodaci((prev) => { return { ...prev, datum: e.target.value } })}></input>
                    <h3>Vrijeme:</h3>
                    <input id="vrijeme" type="text" placeholder="Vrijeme" value = {podaci.vrijeme} onChange={(e) => setPodaci((prev) => { return { ...prev, vrijeme: e.target.value } })}></input>
                </div>
                <div className="pocisti"></div>
                <div className="odvoji"></div>
                <div className="lijevi-inputi">
                <h3>Simptomi:</h3>
                <textarea rows="2" id="simptomi" value = {podaci.simptomi} onChange={(e) => setPodaci((prev) => { return { ...prev, simptomi: e.target.value } })}></textarea> 
                <h3>Fizikalni pregled:</h3>
                <textarea rows="2" id="fizikalni-pregled" value = {podaci.fizikalniPregled} onChange={(e) => setPodaci((prev) => { return { ...prev, fizikalniPregled: e.target.value } })}></textarea> 
                <h3>Komentar:</h3>
                <textarea rows="2" id="komentar" value = {podaci.komentar} onChange={(e) => setPodaci((prev) => { return { ...prev, komentar: e.target.value } })}></textarea> 
                </div>
                <div className="desni-inputi">
                <h3>Dijagnoza:</h3>
                <textarea rows="2" id="dijagnoza" value = {podaci.dijagnoza} onChange={(e) => setPodaci((prev) => { return { ...prev, dijagnoza: e.target.value } })}></textarea> 
                <h3>Tretman:</h3>
                <textarea rows="2" id="tretman" value = {podaci.tretman} onChange={(e) => setPodaci((prev) => { return { ...prev, tretman: e.target.value } })}></textarea> 
                </div>
                <div className="pocisti"></div>
                <button className="uredi-cv" onClick={dodajPregled}>Spasi</button>
            </div>
        </DoktorSideBar>
    );
}

export default DodavanjePregleda;