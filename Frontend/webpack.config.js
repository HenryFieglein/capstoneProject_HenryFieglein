const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyPlugin = require("copy-webpack-plugin");
const { CleanWebpackPlugin } = require('clean-webpack-plugin');

module.exports = {
  optimization: {
    usedExports: true
  },
  entry: {
    examplePage: path.resolve(__dirname, 'src', 'pages', 'examplePage.js'),
    eventPage: path.resolve(__dirname, 'src', 'pages', 'eventPage.js'),
    venuePage: path.resolve(__dirname, 'src', 'pages', 'venuePage.js'),
    rsvpPage: path.resolve(__dirname, 'src', 'pages', 'rsvpPage.js'),
    },
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js',
  },
  devServer: {
    https: false,
    port: 5001,
    open: true,
    openPage: 'http://localhost:5001',
    // diableHostChecks, otherwise we get an error about headers and the page won't render
    disableHostCheck: true,
    contentBase: 'packaging_additional_published_artifacts',
    // overlay shows a full-screen overlay in the browser when there are compiler errors or warnings
    overlay: true,
    proxy: [
      {
        context: [
          '/events',
        ],
        target: 'http://localhost:5001'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/events.html',
      filename: 'events.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/rsvp.html',
      filename: 'rsvp.html',
      inject: false
    }),
    new HtmlWebpackPlugin({
      template: './src/venues.html',
      filename: 'venues.html',
      inject: false
    }),
    new CopyPlugin({
      patterns: [
        {
          from: path.resolve('src/css'),
          to: path.resolve("dist/css")
        }
      ]
    }),
    new CleanWebpackPlugin()
  ]
}
