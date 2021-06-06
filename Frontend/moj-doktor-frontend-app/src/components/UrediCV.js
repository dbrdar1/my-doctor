import React, { useEffect, useState } from 'react';
import { message, Rate } from 'antd';
import '../assets/css/cv.css';
import DoktorSideBar from './DoktorSideBar';
import '../assets/css/dugme.css'
import HeaderNaslovna from './HeaderNaslovna';
import DodavanjeEdukacije from './DodavanjeEdukacije';
import DodavanjeCertifikata from './DodavanjeCertifikata';
import Loader from './Loader';
import Stranica403 from './stranica403';


const UrediCV = () => {
    const [uredjivanjeBiografije, setUredjivanjeBiografije] = useState(false);
    const [dodavanjeEdukacije, setDodavanjeEdukacije] = useState(false);
    const [dodavanjeCertifikata, setDodavanjeCertifikata] = useState(false);
    let [loading, setLoading] = useState(true);
    let [biografija, setBiografija] = useState("");
    let [certifikati, setCertifikati] = useState([]);
    let [edukacije, setEdukacije] = useState([]);
    let [ime, setIme] = useState("");
    let [prezime, setPrezime] = useState("");
    let [adresa, setAdresa] = useState("");
    let [email, setEmail] = useState("");
    let [ocjena, setOcjena] = useState(2.5);
    let [edukacijaState, setEdukacijaState] = useState({
        idDoktora: localStorage.getItem("id"),
        institucija: "",
        odsjek: "",
        stepen: "",
        godinaPocetka: 0,
        godinaZavrsetka: 0,
        grad: "",
        drzava: ""
    });
    let [certifikatState, setCertifikatState] = useState({
        idDoktora: localStorage.getItem("id"),
        institucija: "",
        naziv: "",
        godinaIzdavanja: 0
    });

    const NEMA_PODATAKA = "Još uvijek nema raspoloživih podataka za ovu kategoriju.";

    useEffect(() => {
        if(localStorage.getItem("uloga")!=='DOKTOR') return;
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/doktor-detalji/doktori/' + localStorage.getItem("id"), { method: "get", headers })
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

    const urediBiografijuHandle = () => {
        if (!uredjivanjeBiografije) setUredjivanjeBiografije(true);
        else {
            const headers = new Headers({
                "Authorization": 'Bearer ' + localStorage.getItem("token"),
                "Content-Type": "application/json"
            });
            const unos = { idDoktora: localStorage.getItem("id"), titula: '', biografija }
            fetch('http://localhost:8080/doktor-detalji/uredi-biografiju-titulu', { method: "PUT", body: JSON.stringify(unos), headers })
                .then((res) => {
                    return res.json();
                })
                .then((res) => {
                    if (res.statusniKod === 200) {
                        message.success("Uspješno ste uredili biografiju.");
                        setUredjivanjeBiografije(false);
                    } else {
                        message.error(res.poruka);
                    }
                })
                .catch((err) => {
                    message.error("Došlo je do greške.");
                });
        }
    }

    const dodajCertifikatHandle = () => {
        if (!dodavanjeCertifikata) setDodavanjeCertifikata(true);
        else {
            const headers = new Headers({
                "Authorization": 'Bearer ' + localStorage.getItem("token"),
                "Content-Type": "application/json"
            });
            if (certifikatState.institucija === '' || certifikatState.naziv === '' || certifikatState.godinaIzdavanja < 1900 || certifikatState.godinaIzdavanja > new Date().getFullYear())
                message.error("Neispravni podaci. Provjerite unos i pokušajte ponovo.")
            else
                fetch('http://localhost:8080/doktor-detalji/dodaj-certifikat', { method: "POST", body: JSON.stringify(certifikatState), headers })
                    .then((res) => {
                        return res.json();
                    })
                    .then((res) => {
                        if (res.statusCode === 400) {
                            message.error(res.message);
                            return;
                        }
                        else {
                            message.success("Uspješno ste dodali certifikat.", 2);
                            setDodavanjeCertifikata(false);
                            setCertifikati(prev => [...prev, certifikatState]);
                            setCertifikatState({
                                idDoktora: localStorage.getItem("id"),
                                institucija: "",
                                naziv: "",
                                godinaIzdavanja: 0
                            });
                        }
                    })
                    .catch((err) => {
                        message.error("Došlo je do greške.");
                    });
        }
    }

    const okEdukacija=(e)=>{
        if(e.institucija==='') return "Greska! Unesite ispravno podatak o instituciji.";
        if(e.odsjek==='') return "Greska! Unesite ispravno podatak o odsjeku.";
        if(e.stepen==='') return "Greska! Unesite ispravno podatak o stepenu.";
        if(e.godinaPocetka<1900 || e.godinaPocetka>new Date().getFullYear()) return "Greska! Unesite ispravno godinu početka edukacije.";
        if(e.godinaZavrsetka<1900 || e.godinaZavrsetka>new Date().getFullYear()) return "Greska! Unesite ispravno godinu završetka edukacije.";
        if(e.grad==='') return "Greska! Unesite ispravno podatak o gradu.";
        if(e.drzava==='') return "Greska! Unesite ispravno podatak o državi.";
        return "OK";
    }

    const dodajEdukacijuHandle = () => {
        if (!dodavanjeEdukacije) setDodavanjeEdukacije(true);
        else {
            const headers = new Headers({
                "Authorization": 'Bearer ' + localStorage.getItem("token"),
                "Content-Type": "application/json"
            });
            console.log(edukacijaState)
            if(okEdukacija(edukacijaState)!=="OK") message.error(okEdukacija(edukacijaState));
            else
            fetch('http://localhost:8080/doktor-detalji/dodaj-edukaciju', { method: "POST", body: JSON.stringify(edukacijaState), headers })
                .then((res) => {
                    return res.json();
                })
                .then((res) => {
                    if (res.statusCode === 400) {
                        message.error(res.message);
                        return;
                    }
                    else {
                        message.success("Uspješno ste dodali edukaciju.", 2);
                        setDodavanjeEdukacije(false);
                        setEdukacije(prev => [...prev, edukacijaState]);
                        setEdukacijaState({
                            idDoktora: localStorage.getItem("id"),
                            institucija: "",
                            odsjek: "",
                            stepen: "",
                            godinaPocetka: 0,
                            godinaZavrsetka: 0,
                            grad: "",
                            drzava: ""
                        });
                    }
                })
                .catch((err) => {
                    message.error("Došlo je do greške.");
                });
        }
    }
    
    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar >
            <HeaderNaslovna stranica="Moj CV" />
            <div className="kartica-cv">
                {loading ? <><br /><br /><Loader /></> : <>
                    <div className="osnovni-podaci">
                        <img className="profilna" src="https://hancroft.co.nz/wp-content/uploads/2019/05/profile-placeholder.png" alt="slika profila" />
                        <h2>{ime} {prezime}</h2>
                        <h2>{adresa}</h2>
                        <h2>{email}</h2>
                        <div className="ocjena">
                            <h3>Ocjena:</h3>
                            <div className="ocjena-star">
                                <Rate disabled allowHalf defaultValue={ocjena} style={{ color: '#001529' }} />
                            </div>
                        </div>
                    </div>
                    <div className="cv-detalji">
                        <h2>BIOGRAFIJA</h2>
                        {!uredjivanjeBiografije ?
                            <p> {biografija} </p>
                            :
                            <textarea rows="4" value={biografija} onChange={(e) => { setBiografija(e.target.value) }}>
                            </textarea>
                        }
                        <button className="uredi-cv" onClick={urediBiografijuHandle}>Uredi biografiju</button><br /><br /><br />
                        <h2>EDUKACIJE</h2>
                        <p>
                            {edukacije.length === 0 && <>{NEMA_PODATAKA}</>}

                            <ul>
                                {edukacije.map((edukacija) => (
                                    <li>{edukacija.stepen}, {edukacija.institucija}, {edukacija.odsjek} ({edukacija.godinaPocetka}-{edukacija.godinaZavrsetka})</li>
                                ))}
                            </ul>
                        </p>
                        {dodavanjeEdukacije && <DodavanjeEdukacije edukacija={edukacijaState} setEdukacija={setEdukacijaState} />}
                        <button className="uredi-cv" onClick={dodajEdukacijuHandle}>Dodaj edukaciju</button><br /><br />
                        <h2>CERTIFIKATI</h2>
                        <p>
                            {certifikati.length === 0 && <>{NEMA_PODATAKA}</>}
                            <ul>
                                {certifikati.map((certifikat) => (
                                    <li>{certifikat.naziv}, {certifikat.institucija}, {certifikat.godinaIzdavanja}.</li>
                                ))}
                            </ul>
                        </p>
                        {dodavanjeCertifikata && <DodavanjeCertifikata certifikat={certifikatState} setCertifikat={setCertifikatState} />}
                        <button className="uredi-cv" onClick={dodajCertifikatHandle}>Dodaj certifikat</button>
                    </div></>}
            </div>
        </DoktorSideBar>
    );
}

export default UrediCV;