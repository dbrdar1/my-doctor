import React from 'react';
import '../assets/css/forma.css'

const FormaRegistracija = (props) => {
    let podaci = props.podaci;
    let setPodaci = props.setPodaci;

    return (
        <div className="elementi">
            <div className="lijeva-forma">
                <input id="ime" className="input" type="text" placeholder="Ime"
                    value={podaci.ime} onChange={e => { setPodaci(prev => { return { ...prev, ime: e.target.value } }) }}></input>
                <input id="prezime" className="input" type="text" placeholder="Prezime"
                    value={podaci.prezime} onChange={e => { setPodaci(prev => { return { ...prev, prezime: e.target.value } }) }}></input>
                <input id="datum-rodjenja" className="input" type="date" placeholder="Datum rođenja"
                    value={podaci.datumRodjenja} onChange={e => { setPodaci(prev => { return { ...prev, datumRodjenja: e.target.value } }) }}></input>
                <input id="adresa" className="input" type="text" placeholder="Adresa"
                    value={podaci.adresa} onChange={e => { setPodaci(prev => { return { ...prev, adresa: e.target.value } }) }}></input>

            </div>
            <div className="desna-forma">
                <input id="telefon" className="input" type="text" placeholder="Broj telefona"
                    value={podaci.brojTelefona} onChange={e => { setPodaci(prev => { return { ...prev, brojTelefona: e.target.value } }) }}></input>
                <input id="email" className="input" type="email" placeholder="Email"
                    value={podaci.email} onChange={e => { setPodaci(prev => { return { ...prev, email: e.target.value } }) }}></input>
                <input id="username" className="input" type="text" placeholder="Korisničko ime"
                    value={podaci.korisnickoIme} onChange={e => { setPodaci(prev => { return { ...prev, korisnickoIme: e.target.value } }) }}></input>
                <input id="sifra" className="input" type="password" placeholder="Šifra"
                    value={podaci.lozinka} onChange={e => { setPodaci(prev => { return { ...prev, lozinka: e.target.value } }) }}></input>

            </div>
            <div className="pocisti"></div>
            <input id="chbox" className="chbox" type="checkbox"></input>
            <label className="chbox" for="chbox"> &nbsp;Da li ste doktor?</label>
        </div>
    );
}

export default FormaRegistracija;