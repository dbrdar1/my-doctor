import React, {useState, useEffect} from 'react';
import DoktorSideBar from './DoktorSideBar';
import NaslovnaPregledKartica from './NaslovnaPregledKartica';
import '../assets/css/pacijenti-doktori.css'
import HeaderNaslovna from './HeaderNaslovna';
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';
import { message } from 'antd';
import Stranica403 from './stranica403';
import Loader from './Loader';



const MojiPacijenti = () => {
    let [loading, setLoading] = useState(true);

    const responsive = {
        desktop: {
          breakpoint: { max: 3000, min: 1024 },
          items: 3,
          slidesToSlide: 3 // optional, default to 1.
        }
    }
    
    const [podaci, setPodaci] = useState({
        imePrezime: '', korisnickoIme: '', adresa: '', datumRodjenja: null, brojTelefona: '', email: ''
    });

    const [podaciPacijenata, setPodaciPacijenata] = useState([]);

    const formatirajDatum = (d) => {
        d = new Intl.DateTimeFormat('en-US').format(d);
        let string = d.split('/');
        if (string[0] < 10) string[0] = '0' + string[0]
        if (string[1] < 10) string[1] = '0' + string[1]
        return parseInt(string[2]) + '-' + string[0] + '-' + string[1];
    }

    useEffect(() => {
        if(localStorage.getItem("uloga")!=='DOKTOR') return;
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
                        datumRodjenja: formatirajDatum(res.datumRodjenja),
                        brojTelefona: res.brojTelefona,
                        email: res.email
                    }
                })
                fetch('http://localhost:8080/pregledi-kartoni/pacijenti-doktora/' + res.id, { method: "get", headers })
                .then((res2) => {
                    return res2.json();
                })
                .then((res2) => {
                    let svi=[]
                    res2.forEach(pacijent => {
                        let postoji = false;
                        for (let i=0; i<svi.length; i++) if(svi[i].id===pacijent.id) postoji=true;
                        if(!postoji)
                        svi.push(pacijent);
                     //setLoading(false)
                   });
                   setPodaciPacijenata(svi)
                   setLoading(false);
                })
                .catch(() => {
                    message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
                });

            })
            .catch(() => {
                message.error("Došlo je do greške. Provjerite unesene podatke i pokušajte ponovo.");
            });
    }, []);

    if(localStorage.getItem("uloga")!=='DOKTOR') return(<Stranica403/>)
    return (
        <DoktorSideBar>
            <HeaderNaslovna stranica="Moji pacijenti" />
            {loading ? <><Loader /> </> : 
            <div >
            <Carousel  responsive={responsive} containerClass="carousel-container">
                {podaciPacijenata.map((pacijent, index) => (
                <NaslovnaPregledKartica key={pacijent.id} pacijent={pacijent}  doktor = {true}>
                </NaslovnaPregledKartica> 
                ))}
            </Carousel>
            </div>
            }
        </DoktorSideBar>

    );
}

export default MojiPacijenti;