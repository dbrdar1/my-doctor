import React from 'react';
import { useHistory } from 'react-router';
import '../assets/css/forma.css'

const FormaPrijava = (props) => {
    const history = useHistory();
    return(
        <div className="elementi">
            <input className="input" type="text" placeholder="Korisničko ime" value={props.uname} onChange={e=>props.setUname(e.target.value)}></input>
            <input className="input" type="password" placeholder="Šifra" value={props.sifra} onChange={e=>props.setSifra(e.target.value)}></input>
            <button className="zaboravljena-sifra" onClick={()=>{history.push("/promjena-lozinke")}}>Zaboravili ste lozinku?</button>
        </div>
    );
}

export default FormaPrijava;