import { useHistory } from 'react-router'
import '../assets/css/registracija.css'
import DugmePrijava from './DugmePrijava'
import FormaPrijava from './FormaPrijava'
import { message } from 'antd';
import { useState } from 'react';
import NaslovnaPacijent from './NaslovnaPacijent';
import NaslovnaDoktor from './NaslovnaDoktor';
const axios = require('axios').default;

const Prijava = () => {

    const history = useHistory();
    let [uname, setUname] = useState('');
    let [sifra, setSifra] = useState('');
    const prijaviKorisnika = () => {
        let unos = { korisnickoIme: uname, lozinka: sifra }
        const header = { headers: { "Content-Type": "application/json" } };
        axios.post('http://localhost:8080/prijava', unos, header)
            .then((res) => {
                console.log(res.data)
                localStorage.setItem("token", res.data.token);
                localStorage.setItem("uloga", res.data.uloga)
                localStorage.setItem("id", res.data.idKorisnika)
                const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
                fetch('http://localhost:8080/profil', { method: "get", headers })
                    .then((res) => {
                        return res.json();
                    })
                    .then((res) => {
                        localStorage.setItem("ime", res.ime)
                        localStorage.setItem("prezime", res.prezime)
                        if (localStorage.getItem("uloga") === "PACIJENT") history.push("/pacijent/naslovna");
                        else history.push("/doktor/naslovna");
                    })
                    .catch(() => {
                        message.error("Došlo je do greške.");
                    });
            })
            .catch(() => {
                message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });
    }

    if (localStorage.getItem("uloga") === 'PACIJENT') return (<NaslovnaPacijent />)
    else if (localStorage.getItem("uloga") === 'DOKTOR') return (<NaslovnaDoktor />)
    return (
        <div className="blok">
            <div className="opis-prijava opis">
                <h4>Moj doktor</h4>
                <div className="poruka">
                    <br /><br />
                    <h2>Dobro došli!</h2>
                    <p>“Kada zdravlja nema, mudrost se ne može otkriti, umjetnost se ne može manifestirati, snaga ne može da se bori, bogatstvo postaje beskorisno i inteligencija se ne može primijeniti.” - <i>Herofil</i> </p>
                    <DugmePrijava klasa={"registracija-prijava"} tekst={"Registruj se"} onClick={() => { history.push("/registracija") }} />
                </div>
            </div>
            <div className="forma">
                <h2>Prijava</h2>
                <FormaPrijava uname={uname} sifra={sifra} setUname={setUname} setSifra={setSifra} /> <br />
                <DugmePrijava klasa={"registracija-registracija"} tekst={"Prijavi se"} onClick={prijaviKorisnika} />
            </div>
        </div>
    );
}

export default Prijava;