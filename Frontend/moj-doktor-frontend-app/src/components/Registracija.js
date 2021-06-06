import React, { useState } from 'react';
import '../assets/css/registracija.css';
import DugmePrijava from './DugmePrijava';
import FormaRegistracija from './FormaRegistracija';
import { useHistory } from 'react-router';
import { message } from 'antd';
import 'antd/dist/antd.css';
const axios = require('axios').default;

const Registracija = () => {
    const history = useHistory();
    let [podaci, setPodaci] = useState({
        ime: '', prezime: '', datumRodjenja: null, adresa: '', brojTelefona: '',
        email: '', korisnickoIme: '', lozinka: '', uloga: ''
    });


    const registrujKorisnika = () => {
        let unos = podaci;
        if(unos.ime==='') {message.error("Niste unijeli ime!"); return}
        if(unos.prezime==='') {message.error("Niste unijeli prezime!"); return}
        if(unos.email==='') {message.error("Niste unijeli email!"); return}
        if(unos.brojTelefona==='') {message.error("Niste unijeli broj telefona!"); return}
        if(unos.korisnickoIme==='') {message.error("Niste unijeli korisničko ime!"); return}
        if(unos.lozinka==='') {message.error("Niste unijeli lozinku!"); return}

        if (document.getElementById("chbox").checked) unos.uloga = 'DOKTOR';
        else unos.uloga = 'PACIJENT';

        const header = { headers: { "Content-Type": "application/json" } };
       // console.log(JSON.stringify(unos));

        axios.post('http://localhost:8080/registracija', unos, header)
            .then(() => {
                console.log("REGGG")
                console.log(unos)
                message.success("Uspješno ste se registrovali.");
                history.push("/");
            })
            .catch((err) => {
                message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });

    }

    return (
        <div className="blok">
            <div className="opis-registracija opis">
                <h4>Moj doktor</h4>
                <div className="poruka">
                    <br /><br />
                    <h2>Dobro došli!</h2>
                    <p>Komuniciranje sa doktorom ili pacijentom nikad nije bilo lakše. Postanite dio platforme MojDoktor i uvjerite se.</p>
                    <DugmePrijava klasa={"registracija-prijava"} tekst={"Prijavi se"} onClick={() => { history.push("/") }} />
                </div>
            </div>
            <div className="forma">
                <h2>Registracija</h2>
                <FormaRegistracija podaci={podaci} setPodaci={setPodaci} /> <br />
                <DugmePrijava klasa={"registracija-registracija"} tekst={"Registruj se"} onClick={registrujKorisnika} />
            </div>
        </div>
    );
}

export default Registracija;