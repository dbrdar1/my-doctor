import React from 'react';
import '../assets/css/edu-cert.css'

const DodavanjeCertifikata = (props) => {
    let { certifikat, setCertifikat } = props;
    return (<div className="unos">
        <h2>Odgovorna institucija*</h2>
        <input type="text" placeholder="Odgovorna institucija" value={certifikat.institucija}
            onChange={e => setCertifikat(prev => { return { ...prev, institucija: e.target.value } })}
        ></input>
        <br />
        <h2>Naziv certifikata*</h2>
        <input type="text" placeholder="Naziv certifikata" value={certifikat.naziv}
            onChange={e => setCertifikat(prev => { return { ...prev, naziv: e.target.value } })}
        ></input>
        <br />
        <h2>Godina izdavanja*</h2>
        <input type="number" value={certifikat.godinaIzdavanja}
            onChange={e => setCertifikat(prev => { return { ...prev, godinaIzdavanja: e.target.value } })}
        ></input>
        <br />
    </div>);
}

export default DodavanjeCertifikata;