import React, { useEffect, useState } from 'react';
import { useHistory } from 'react-router';
import '../assets/css/termin.css';
import '../assets/css/karton.css';
import '../assets/css/pretraga.css';
import { Table, Button, Tooltip } from 'antd';
import PacijentSideBar from './PacijentSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import Loader from './Loader';
import { SearchOutlined } from '@ant-design/icons';
import Stranica403 from './stranica403';

const SviDoktori = () => {

    const history = useHistory();

    let [loading, setLoading] = useState(true);
    let [pretraga, setPretraga] = useState('');
    let [sviDoktori, setSviDoktori] = useState([]);
    const [data, setData] = useState([]);

    const preusmjeriNaChatSaDoktorom = (idDoktoraSagovornika, imePrezimeSagovornika) => {
        history.push({ pathname: "/pacijent/chat/" + idDoktoraSagovornika, state: { imePrezimeSagovornika: imePrezimeSagovornika }});
    };

    const [columns, setColumns] = useState([
        {
            title: 'Ime i prezime doktora',
            dataIndex: 'imePrezime',
        },
        {
            title: 'Akcije',
            dataIndex: 'akcije',
            render: (tag, record) => <Button type="default" key={tag} onClick={() => { preusmjeriNaChatSaDoktorom(record.key, record.imePrezime); }}>{tag}</Button>
        }
    ]);

    useEffect(() => {
        if(localStorage.getItem("uloga")!='PACIJENT') return;
        fetch('http://localhost:8080/korisnici?uloga=DOKTOR', { method: "get" })
            .then((res) => {
                return res.json();
            })
            .then((res) => {
                let elementi = [];
                res.forEach(element => {
                    let item = {
                        imePrezime: element.ime + ' ' + element.prezime,
                        key: element.id,
                        akcije: 'Kontaktiraj doktora'
                    }
                    elementi.push(item)
                });
                setData(elementi);
                setSviDoktori(elementi);
                setLoading(false);
            });
    }, []);

    const filtrirajDoktore = () => {
        if (pretraga.length == 0 && data !== sviDoktori) { setData(sviDoktori); return; }
        let filtrirani = []
        for (let i = 0; i < sviDoktori.length; i++)
            if (sviDoktori[i].imePrezime.toLowerCase().includes(pretraga.toLowerCase())) {
                filtrirani.push(sviDoktori[i]);
            }
        setData(filtrirani);
    }

    if(localStorage.getItem("uloga")!='PACIJENT') return(<Stranica403/>)
    return (
        <PacijentSideBar>
            <HeaderNaslovna stranica="Doktori" />
            <div className="naslovna-sadrzaj">
                <div className="tabela-termina">
                    {loading ? <Loader /> :
                        <>
                            <Tooltip title="Unesite bilo koji dio imena i/ili prezimena doktora. Za povratak na sve doktore, pokrenite pretragu bez unesenog pojma.">
                                <input type="search" className="pretraga" placeholder="Pojam za pretragu" value={pretraga} onChange={(e) => setPretraga(e.target.value)}></input>
                            </Tooltip>
                            <Button shape="circle" icon={<SearchOutlined />} onClick={filtrirajDoktore} />

                            <Table columns={columns} dataSource={data} pagination={{ position: ['None', 'bottomCenter'], pageSize: 5 }} />
                        </>
                    }
                </div>
            </div>
        </PacijentSideBar>
    );
}


export default SviDoktori;