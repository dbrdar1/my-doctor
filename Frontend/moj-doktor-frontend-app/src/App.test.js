import { render, screen } from '@testing-library/react';
import App from './App';
import KarticaNaslovna from './components/KarticaNaslovna';
import Registracija from './components/Registracija';
import Stranica403 from './components/stranica403';
import Stranica404 from './components/stranica404';

test('pocetna stranice aplikacije', () => {
  render(<App />);
  expect(screen.getByText("Dobro došli!")).toBeInTheDocument();
  expect(screen.getByText("Prijava")).toBeInTheDocument();
  expect(screen.getByText("Zaboravili ste lozinku?")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Korisničko ime")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Šifra")).toBeInTheDocument();
});

test('registracija', () => {
  render(<Registracija />);
  expect(screen.getByText("Dobro došli!")).toBeInTheDocument();
  expect(screen.getByText("Registracija")).toBeInTheDocument();
  expect(screen.getByText("Da li ste doktor?")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Ime")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Prezime")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Email")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Šifra")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Adresa")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Korisničko ime")).toBeInTheDocument();
  expect(screen.getByPlaceholderText("Broj telefona")).toBeInTheDocument();
});

test('403', () => {
  render(<Stranica403 />);
  expect(screen.getByText("403")).toBeInTheDocument();
  expect(screen.getByText("Greška pri autorizaciji. Zabranjen pristup stranici.")).toBeInTheDocument();
  expect(screen.getByText("Nazad")).toBeInTheDocument();
});

test('404', () => {
  render(<Stranica404 />);
  expect(screen.getByText("404")).toBeInTheDocument();
  expect(screen.getByText("Stranica kojoj pokušavate pristupiti nije dostupna.")).toBeInTheDocument();
  expect(screen.getByText("Nazad")).toBeInTheDocument();
});

test('kartica za dashboard', () => {
  render(<KarticaNaslovna naziv="Testna kartica" opis="Opis testne kartice"  />);
  expect(screen.getByText("Testna kartica")).toBeInTheDocument();
  expect(screen.getByText("Opis testne kartice")).toBeInTheDocument();
  expect(screen.getByTestId("ikona-kartica")).toBeInTheDocument();
});