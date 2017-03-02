package io.discloader.discloader.network.voice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.NoRouteToHostException;

import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

/**
 * @author Perry Berman
 *
 */
public class StreamSender {
    private final StreamProvider provider;
    private DatagramSocket udpSocket;
    private Thread packetThread;
    private VoiceWebSocket ws;

    public StreamSender(StreamProvider streamer) {
        this.provider = streamer;
        this.ws = provider.ws;
        // this.udpSocket = streamer.conection.getUdpClient().udpSocket;
    }

    public void sendPackets() {
        udpSocket = provider.conection.getUdpClient().udpSocket;
        packetThread = new Thread("Some stream") {
            @Override
            public void run() {
                long lastSent = System.currentTimeMillis();

                while (!udpSocket.isClosed() && !packetThread.isInterrupted()) {
                    try {
                        if ((System.currentTimeMillis() - lastSent) < 20) {
                            AudioFrame frame = provider.conection.player.provide();

                            if (frame != null) {
                            	provider.ws.setSequence(provider.ws.getSequence() + 1);
                                char seq = (char) provider.ws.getSequence();
                                provider.ws.getSecretKey();
                                DatagramPacket send = new StreamPacket(seq, (int) System.currentTimeMillis(), provider.conection.getSSRC(), frame.data).toEncryptedPacket(provider.udpClient.voice_gateway, provider.ws.getSecretKey());
                                udpSocket.send(send);
                            }
                        }
                    } catch (NoRouteToHostException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        long sleepTime = 20 - (System.currentTimeMillis() - lastSent);
                        if (sleepTime > 0) {
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        if (System.currentTimeMillis() < lastSent + 60) {
                            lastSent += 20;
                        } else {
                            lastSent = System.currentTimeMillis();
                        }
                    }

                }
            }
        };

        packetThread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
        packetThread.setDaemon(true);
        packetThread.start();
    }

    public void close() {
        if (packetThread != null) {
            packetThread.interrupt();
        }
    }

}