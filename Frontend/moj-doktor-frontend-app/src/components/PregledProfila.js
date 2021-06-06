import React, { useEffect, useState } from 'react';

const PregledProfila = () => {
    const [podaci, setPodaci] = useState({
        imePrezime: '', korisnickoIme: '', adresa: '', datumRodjenja: null, brojTelefona: '', email: ''
    });
    const ispisDatuma = (d) => {
        d = new Intl.DateTimeFormat('en-US').format(d);
        let string = d.split('/');
        if (string[0] < 10) string[0] = '0' + string[0]
        if (string[1] < 10) string[1] = '0' + string[1]
        return string[1] + '.' + string[0] + '.' + string[2] + '.';
    }
    useEffect(() => {
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/profil', { method: "get", headers })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                setPodaci((prev) => {
                    return {
                        ...prev,
                        imePrezime: res.ime + ' ' + res.prezime,
                        korisnickoIme: res.korisnickoIme,
                        adresa: res.adresa,
                        datumRodjenja: ispisDatuma(res.datumRodjenja),
                        brojTelefona: res.brojTelefona,
                        email: res.email
                    }
                })
            })
            .catch(() => {
                // message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });
    }, []);

    return (
        <div className="kartica-profil">
            <div className="slika-ime">
                <div className="profilna-div">
                    <img className="profilna" src="https://hancroft.co.nz/wp-content/uploads/2019/05/profile-placeholder.png" alt="slika profila" />
                </div>
                <div className="imena">
                    <label for="ime-prezime">Ime i prezime: </label>
                    <input id="ime-prezime" type="text" disabled={true} value={podaci.imePrezime}></input> <br />
                    <label for="korisnicko-ime">Korisničko ime: </label>
                    <input id="korisnicko-ime" type="text" disabled={true} value={podaci.korisnickoIme}></input>
                </div>
                <div className="pocisti" />
            </div>
            <div className="ostala-polja">
                <label for="datum-rodjenja">Datum rođenja: </label>
                <input id="datum-rodjenja" type="text" disabled={true} value={podaci.datumRodjenja}></input> <br />
                <label for="adresa">Adresa: </label>
                <input id="adresa" type="text" disabled={true} value={podaci.adresa}></input><br />
                <label for="broj-telefona">Broj telefona: </label>
                <input id="broj-telefona" type="text" disabled={true} value={podaci.brojTelefona}></input><br />
                <label for="mail">E-mail adresa: </label>
                <input id="mail" type="email" disabled={true} value={podaci.email}></input>
            </div>
        </div>
    );
}

export default PregledProfila;