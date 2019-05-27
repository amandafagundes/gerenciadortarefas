import React, { Component } from "react";
import { MDBCollapse } from "mdbreact";
import api from "../../services/api";
import "./styles.css";
import { Link } from 'react-router-dom';
import axios from 'axios';
import { Alert } from 'reactstrap';
var base64ToImage = require('base64-to-image');

function _imageEncode(arrayBuffer) {
  let u8 = new Uint8Array(arrayBuffer)
  let b64encoded = btoa([].reduce.call(new Uint8Array(arrayBuffer), function (p, c) { return p + String.fromCharCode(c) }, ''))
  console.log('********' + b64encoded);
  let mimetype = "image/jpg";
  return "data:" + mimetype + ";base64," + b64encoded
}

class Item extends Component {
  state = {
    tasks: [],
    collapseID: '',
    status: '',
    success: '',
    submited: false,
  }

  loadImages = () => {
    this.state.tasks.map(async (task) => {
      var response = await axios({
        method: 'get',
        url: 'http://localhost:8080/task/image',
        responseType: 'arraybuffer',
        params: {
          id: task.id
        }
      });
      task.image = response.data != null ? _imageEncode(response.data) : null;
    })
  }


  toggleCollapse = collapseID => () => {
    this.setState(prevState => ({
      collapseID: prevState.collapseID !== collapseID ? collapseID : ""
    }));
  }

  toggleStatus = status => async () => {
    var param = {
      status: status,
    }
    this.setState(({
      status: status,
      tasks: []
    }))
    var response = await api.post('/task/list', status != '' ? param : null);
    this.setState({ tasks: response.data });
    this.loadImages();
  }

  deleteTask = id => async () => {
    var param = {
      id: id,
    }
    this.setState( prevState =>({
      submited: true,
      status: prevState.status,
      tasks: [],
    }))
    var response = await api.delete('/task/delete/' + id);
    if (response.status == 200) this.setState({ success: true });
    else this.setState({ succes: false });
    response = await api.post('/task/list', this.state.status != '' ? param : null);
    this.setState({ tasks: response.data });
  }

  componentDidMount() {
    this.loadTasks();
  }

  loadTasks = async () => {
    const response = await api.post('/task/list', this.state.status);
    this.setState({ tasks: response.data });
    this.loadImages();
  };

  //escuta as alterações do state
  render() {
    const { success, submited, tasks } = this.state;

    return (
      <div id="main">
        <div className="Navbar">
          <nav className="nav-items">
            <div className="nav-item">
              <a onClick={this.toggleStatus('')}>Todas</a>
            </div>
            <div id="pending" className="nav-item">
              <a onClick={this.toggleStatus('PENDING')}>Pendentes</a>
            </div>
            <div className="nav-item">
              <a onClick={this.toggleStatus('COMPLETE')}>Feitas</a>
            </div>
          </nav>
        </div>

        <Message success={success} submited={submited} />

        <div className="tasks-list">
          {tasks.map(task => (
            <article key={task.id}>
              <div className="row">
                <Title onClick={this.toggleCollapse('' + task.id + '')} task={task} />
                <Link id="btn-delete" onClick={this.deleteTask(task.id)}>Excluir</Link>
              </div>
              <MDBCollapse id={'' + task.id + ''} isOpen={this.state.collapseID}>
                {
                  task.image != null &&
                  <p><img src={task.image} /></p>
                }
              </MDBCollapse>
            </article>
          ))}
        </div>

        <Link id="btn-new" to="/task/new">Cadastrar Nova Tarefa</Link>

      </div>
    )
  }
}

function Title(props) {
  if (props.task.status == "COMPLETE") {
    return <strike onClick={props.onClick}>{props.task.title}</strike>
  } else {
    return <normal onClick={props.onClick}>{props.task.title}</normal>
  }
}

function Message(props) {
  if (props.submited) {
    if (!props.success) {
      return (<Alert color="danger">Falha na comunicação com o servidor!</Alert>);
    } else {
      return (<Alert color="success">Tarefa excluída com sucesso!</Alert>);
    }
  } else {
    return null;
  }
}

export default Item;