import React, { Component } from "react";
import "./styles.css";
import api from '../../services/api'
import { Alert } from 'reactstrap';

class Form extends Component {
    state = {
        value: '',
        success: '',
        submited: false,
    }

    handleChange = event => {
        this.setState({ submited: false });
        this.setState({ value: event.target.value });
    }

    handleSubmit = event => {
        event.preventDefault();
        var task = {
            'title': this.state.value,
            'status': 'PENDING'
        }
        if (this.state.value != '') {
            api.post('task/create', task).then(response => {
                console.log(response);
                if (response.status == 200) {
                    this.setState({ success: true });
                    console.log('success ' + this.state.success);
                } else {
                    this.setState({ success: false });
                    console.log('error ' + this.state.success);
                }
            })
        }
        this.setState({ submited: true });
    };

    render() {
        const { value, success, submited } = this.state;
        return (
            <div id="container">
                <form id="form" onSubmit={this.handleSubmit}>
                    <Message success={success} value={value} submited={submited} />
                    <input id="input-title" type="text" placeholder='Nome da Tarefa' value={value} onChange={this.handleChange} />
                    <p>
                        <input id="submit" type="submit" value="Cadastrar" />
                    </p>
                </form>
            </div>
        );
    }
}

function Message(props) {
    if (props.submited) {
        if (props.value == '' && props.success != '') {
            return (<Alert color="danger">Informe o nome da Tarefa!</Alert>);
        } else if (!props.success && props.value != '') {
            return (<Alert color="danger">Falha na comunicação com o servidor!</Alert>);
        } else {
            return (<Alert color="success">Tarefa cadastrada com sucesso!</Alert>);
        }
    } else {
        return null;
    }
}
export default Form;