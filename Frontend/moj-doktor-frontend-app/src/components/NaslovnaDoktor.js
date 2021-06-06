import React, { useEffect } from 'react';
import { useHistory } from 'react-router';
import Naslovna from './Naslovna';
import Stranica403 from './stranica403';

const NaslovnaDoktor = () => {
    const history = useHistory();
    const podaci = {
        imePrezime: '', korisnickoIme: '', adresa: '', datumRodjenja: null, brojTelefona: '', email: ''
    }

    const formatirajDatum = (d) => {
        d = new Intl.DateTimeFormat('en-US').format(d);
        let string = d.split('/');
        if(string[0]<10) string[0]='0'+string[0]
        if(string[1]<10) string[1]='0'+string[1]
        return string[2]+'-'+string[0]+'-'+string[1];
    }
    


    useEffect(() => {
        const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
        fetch('http://localhost:8080/profil', { method: "get", headers })
            .then((res1) => {
                return res1.json();
            })
            .then((res1) => {
                // doktor - podaci kao korisnik
                podaci.imePrezime = res1.ime + ' ' + res1.prezime;
                podaci.korisnickoIme = res1.korisnickoIme;
                podaci.adresa = res1.adresa;
                podaci.datumRodjenja = formatirajDatum(res1.datumRodjenja);
                podaci.brojTelefona = res1.brojTelefona;
                podaci.email = res1.email;
                
            })
            .catch(() => {
                //message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });
    }, []);


    const kartice = [
        {naziv:'Moj profil', opis:'Moj profil', ikona:'https://img.icons8.com/cotton/2x/gender-neutral-user--v2.png', onClick: () => { history.push("/doktor/profil")}},
        {naziv:'Moj CV', opis:'Moj CV', ikona:'https://img.icons8.com/cotton/2x/file.png', onClick: () => { history.push("/doktor/cv")}},
        {naziv:'Raspored termina', opis:'Moji termini', ikona:'https://img.icons8.com/cotton/2x/calendar.png', onClick: () => { history.push("/doktor/termini")}},
        {naziv:'Moji pacijenti', opis:'Moji pacijenti', ikona:'https://cdn4.iconfinder.com/data/icons/medical-business/512/medical_help-512.png', onClick: () => { history.push({ pathname: "/moji-pacijenti"}) }},
        {naziv:'Dodaj pregled', opis:'Novi pregled', ikona:'https://st3.depositphotos.com/24699262/35632/v/950/depositphotos_356326714-stock-illustration-vector-flat-stethoscope-icon-outline.jpg', onClick: () => { history.push({ pathname: "/pregled/dodaj"}) }},
        {naziv:'Razgovori', opis:'Moji razgovori', ikona:'https://img.icons8.com/cotton/2x/chat.png', onClick: () => { history.push("/doktor/razgovori") } }
    ];
    
    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return(
        <Naslovna kartice={kartice} doktor={true} />
    );
}

export default NaslovnaDoktor;