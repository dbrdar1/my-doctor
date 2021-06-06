import React, { useEffect, useState } from 'react';
import { message, Rate } from 'antd';
import '../assets/css/cv.css';
import { useHistory } from 'react-router';
import Loader from './Loader';

const PregledCV = (props) => {
    const history = useHistory();

    let [loading, setLoading] = useState(true);
    let [biografija, setBiografija] = useState("");
    let [certifikati, setCertifikati] = useState([]);
    let [edukacije, setEdukacije] = useState([]);
    let [ime, setIme] = useState("");
    let [prezime, setPrezime] = useState("");
    let [adresa, setAdresa] = useState("");
    let [email, setEmail] = useState("");
    let [ocjena, setOcjena] = useState(2.5);

    const NEMA_PODATAKA = "Još uvijek nema raspoloživih podataka za ovu kategoriju.";

    useEffect(() => {
        console.log(props);
        let URL = 'http://localhost:8080/doktor-detalji/doktori/';
        if (localStorage.getItem("uloga") === 'DOKTOR') URL += localStorage.getItem("id");
        else URL += props.idDoktora;
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch(URL, { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                console.log(res)
                if (res.biografija === '') setBiografija(NEMA_PODATAKA)
                else setBiografija(res.biografija)
                setCertifikati(res.certifikati);
                setEdukacije(res.edukacije);
                setIme(res.ime);
                setPrezime(res.prezime);
                setAdresa(res.adresa);
                setEmail(res.email);
                if (res.ocjena !== 'NaN') setOcjena(res.ocjena);
                setLoading(false);
            })
            .catch(() => {
                message.error("Došlo je do greške pri učitavanju podataka. Pokušajte ponovo.");
            });
    }, []);


    return (
        <div className="kartica-cv">
            {loading ? <><br /><br /><Loader /></> :
                <>
                    <div className="osnovni-podaci">
                        <img className="profilna" src="https://hancroft.co.nz/wp-content/uploads/2019/05/profile-placeholder.png" alt="slika profila" />
                        <h2>{ime} {prezime}</h2>
                        <h2>{adresa}</h2>
                        <h2>{email}</h2>
                        <div className="ocjena">
                            <h3>Ocjena:</h3>
                            <div className="ocjena-star">
                                <Rate allowHalf disabled defaultValue={ocjena} style={{ color: '#001529' }} />
                            </div>
                        </div>
                    </div>
                    <div className="cv-detalji">
                        <h2>BIOGRAFIJA</h2>
                        <p> {biografija} </p>
                        <h2>EDUKACIJE</h2>
                        <p>
                            {edukacije.length === 0 && <>{NEMA_PODATAKA}</>}
                            <ul>
                                {edukacije.map((edukacija) => (
                                    <li>{edukacija.stepen}, {edukacija.institucija}, {edukacija.odsjek} ({edukacija.godinaPocetka}-{edukacija.godinaZavrsetka})</li>
                                ))}
                            </ul>
                        </p>
                        <h2>CERTIFIKATI</h2>
                        <p>
                            {certifikati.length === 0 && <>{NEMA_PODATAKA}</>}
                            <ul>
                                {certifikati.map((certifikat) => (
                                    <li>{certifikat.naziv}, {certifikat.institucija}, {certifikat.godinaIzdavanja}.</li>
                                ))}
                            </ul>
                        </p>
                    </div>
                    {props.doktor && <button className="uredi-cv" onClick={() => history.push("/doktor/cv/uredi")}>Uredi CV</button>}
                </>}
        </div>
    );
}

export default PregledCV;