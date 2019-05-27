import React from 'react';
import {BrowserRouter, Switch, Route} from 'react-router-dom';
import Main from './pages/main';
import Form from './pages/form';

const Routes = () =>(
    <BrowserRouter>
    <Switch>
    <Route exact path="/" component={Main}></Route>
    <Route path="/task/new" component={Form}></Route>
    </Switch>
    </BrowserRouter>
)
export default Routes;