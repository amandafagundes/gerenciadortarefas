import React, { Component } from 'react';
import Header from './components/Header';
import Item from './components/Item';
import Main from './pages/main'
import Routes from './routes';

//sateless
const App = () => (
  <div className="App">
    <Header/>
    <Routes/>
  </div>
);

export default App;
