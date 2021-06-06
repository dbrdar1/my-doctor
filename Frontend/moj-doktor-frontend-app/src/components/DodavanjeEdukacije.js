import React from 'react';
import '../assets/css/edu-cert.css'

const DodavanjeEdukacije = (props) => {
    let { edukacija, setEdukacija } = props;

    return (<div className="unos">
        <h2>Obrazovna institucija*</h2>
        <input type="text" placeholder="Obrazovna institucija" value={edukacija.institucija}
            onChange={e => setEdukacija(prev => { return { ...prev, institucija: e.target.value } })}
        ></input>
        <br />
        <h2>Odsjek*</h2>
        <input type="text" placeholder="Odsjek" value={edukacija.odsjek}
            onChange={e => setEdukacija(prev => { return { ...prev, odsjek: e.target.value } })}

        ></input>
        <br />
        <h2>Stepen edukacije*</h2>
        <input type="text" placeholder="Stepen edukacije" value={edukacija.stepen}
            onChange={e => setEdukacija(prev => { return { ...prev, stepen: e.target.value } })}
        ></input>
        <br />
        <h2>Godina početka*</h2>
        <input type="number" value={edukacija.godinaPocetka}
            onChange={e => setEdukacija(prev => { return { ...prev, godinaPocetka: e.target.value } })}
        ></input>
        <br />
        <h2>Godina završetka*</h2>
        <input type="number" value={edukacija.godinaZavrsetka}
            onChange={e => setEdukacija(prev => { return { ...prev, godinaZavrsetka: e.target.value } })}
        ></input>
        <br />
        <h2>Grad*</h2>
        <input type="text" placeholder="Grad" value={edukacija.grad}
            onChange={e => setEdukacija(prev => { return { ...prev, grad: e.target.value } })}
        ></input>
        <br />
        <h2>Država*</h2>
        <input type="text" placeholder="Država" value={edukacija.drzava}
            onChange={e => setEdukacija(prev => { return { ...prev, drzava: e.target.value } })}
        ></input>
        <br />
    </div>);
}

export default DodavanjeEdukacije;