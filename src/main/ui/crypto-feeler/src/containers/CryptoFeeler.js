import React, { Component } from 'react';
import GaugeChart from 'react-gauge-chart';
import styles from './CryptoFeeler.module.css';

class CryptoFeeler extends Component {

  componentDidMount() {

  }

  render() {

    const style = {
      width: '100vw',
      height: '100vw'
    };

    return (
      <div>
        <GaugeChart id="gauge-chart4"
          nrOfLevels={10}
          arcPadding={0.1}
          cornerRadius={3}
          percent={0.6}
          hideText={true}
          style={style}
        />
      </div>)
  }
}

export default CryptoFeeler;