import React, { useEffect, useState } from 'react';
import PacijentSideBar from './PacijentSideBar';
import HeaderNaslovna from './HeaderNaslovna';
import '../assets/css/termin.css';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import { message } from 'antd';
import { Table, Button } from 'antd';
import Loader from './Loader';
import { Modal } from 'antd';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import Stranica403 from './stranica403';
const { confirm, error } = Modal;

const SERVER_URL = 'http://localhost:8083/ws';
let stompClient;

const PregledTerminaPacijent = () => {
  let [data, setData] = useState([]);
  let [loading, setLoading] = useState(true);


  const ispisiDatum = (d) => {
    const datum = new Date(d);
    return datum.getDate() + '.' + (datum.getMonth() + 1) + '.' + datum.getFullYear() + '.';
  }

  const okDatum = (datum) => {
    let d = datum.split('.');
    d = new Date(d[2] + '-' + d[1] + '-' + d[0]);
    const today = new Date()
    const tomorrow = new Date(today)
    tomorrow.setDate(tomorrow.getDate() + 1)
    const danas = d.getDate() === today.getDate() && d.getFullYear() === today.getFullYear()
    const sutra = d.getDate() === tomorrow.getDate() && d.getFullYear() === tomorrow.getFullYear()
    console.log("dns: ", danas, " sutra: ", sutra)
    return !(danas || sutra)
  }

  const obrisiTermin = (record) => {
    const id = record.key;
    const datum = record.datum;    if (okDatum(datum))
      confirm({
        title: 'Da li ste sigurni da želite otkazati ovaj termin?',
        icon: <ExclamationCircleOutlined />,
        content: 'Nakon klika na OK, odabrani termin će biti obrisan.',
        onOk() {
          const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
          fetch('http://localhost:8080/termini/obrisi-termin/' + id, { method: "DELETE", headers })
            .then((res) => {
              return res.json();
            })
            .then((res) => {
              if (res.statusCode === 400) {
                message.error(res.message);
                return;
              }
              else {
                message.success("Uspješno ste otkazali termin.", 2);
                sendNotification(record.recipientId, record.datum, record.vrijeme)
                window.location.reload();
              }
            })
        },
        onCancel() { },
      });
    else
      error({
        title: 'Nemoguće otkazati termin!',
        content: (
          <p>
            Termin koji pokušavate otkazati je zakazan za danas ili sutra. Više nemate mogućnost njegovog otkazivanja.
          </p>
        ),
      });
  }

  const [columns, setColumns] = useState([
    {
      title: 'Ime i prezime doktora',
      dataIndex: 'doktor',
      key: 'doktor',

    },
    {
      title: 'Datum',
      dataIndex: 'datum',
      key: 'datum',

    },
    {
      title: 'Vrijeme',
      dataIndex: 'vrijeme',
      key: 'vrijeme',

    },
    {
      title: 'Akcije',
      key: 'akcije',
      dataIndex: 'akcije',
      render: (tag, record) => <Button type="primary" danger key={tag} onClick={() => obrisiTermin(record)}>{tag}</Button>
    },
  ]);

  useEffect(() => {
    if (localStorage.getItem("uloga") !== 'PACIJENT') return;
    const headers = new Headers({ "Authorization": 'Bearer ' + localStorage.getItem("token") });
    fetch('http://localhost:8080/termini/termini-pacijenta/' + localStorage.getItem("id"), { method: "get", headers })
      .then((res) => {
        return res.json();
      })
      .then((res) => {
        console.log(res)
        const resTermini = res.termini;
        let termini = []
        resTermini.forEach(termin => {
          const item = {
            key: termin.id,
            recipientId: termin.doktor.id,
            doktor: termin.doktor.ime + ' ' + termin.doktor.prezime,
            datum: termin.datum,
            vrijeme: termin.vrijeme,
            akcije: 'Otkaži'
          }
          termini.push(item)
        });
        termini.sort(
          function (a, b) {
            return new Date(a.datum) > new Date(b.datum) ? 1 : -1;
          });
        for (let i = 0; i < termini.length; i++) {
          const datumTermina = new Date(termini[i].datum)
          datumTermina.setHours(0, 0, 0, 0)
          const danas = new Date()
          danas.setHours(0, 0, 0, 0)
          if (datumTermina < danas) {
            termini.splice(i, 1);
            i--;
            continue;
          }
          else {
            console.log("formatiram ", termini[i].datum)
            termini[i].datum = ispisiDatum(termini[i].datum)
          }
        }
        setData(termini)
        setLoading(false);
        connectToWebSocket()

      })
      .catch(() => {
        message.error("Došlo je do greške pri učitavanju podataka. Pokušajte ponovo.");
      });
      return () => { disconnectFromWebSocket(); };
  }, []);

  const connectToWebSocket = () => {
    connect();
  };

  const disconnectFromWebSocket = () => {
    disconnect();
  };

  function connect() {
    var socket = new SockJS(SERVER_URL);
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
  }

  const onConnected = (frame) => {
    console.log('Connected: ' + frame);
  };

  const onError = (err) => {
    console.log(err);
  };

  const disconnect = () => {
    if (stompClient !== null) {
      stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  function sendNotification(recipientId, datumTermina, vrijemeTermina) {
    const naslov = "Obavještenje: Termin otkazan"
    const tekst = "Pacijent " + localStorage.getItem("ime") + ' ' + localStorage.getItem("prezime") +
      ' otkazao je termin ' + datumTermina + ' u ' + vrijemeTermina + 'h.'
    const newNotification = {
      senderId: Number(localStorage.getItem("id")),
      recipientId: recipientId,
      naslov: naslov,
      tekst: tekst,
      datum: "datum",
      vrijeme: "00:00"
    };
    stompClient.send("/app/notifikacije", {}, JSON.stringify(newNotification));
    console.log(newNotification);
  }

  if (localStorage.getItem("uloga") !== 'PACIJENT') return (<Stranica403 />)
  return (
    <PacijentSideBar>
      <HeaderNaslovna stranica="Moji termini" />
      <div className="naslovna-sadrzaj">
        <div className="tabela-termina">
          {loading ? <Loader /> :
            <Table columns={columns} dataSource={data} pagination={{ position: ['None', 'bottomCenter'], pageSize: 5 }} />}                </div>
      </div>
    </PacijentSideBar>
  );
}

export default PregledTerminaPacijent;