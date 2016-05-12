package net.md_5.bungee.protocol;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

public class Varint21FrameDecoder extends ByteToMessageDecoder
{

    /**
     * Accept 5 empty packets per second (wait 200 milliseconds)
     * <p/>
     * The user may override this by setting 'waterfall.empty_packet_throttle' to the number of milliseconds to wait between throttlse
     */
    private static final long EMPTY_PACKET_THROTTLE = Long.parseLong(System.getProperty("waterfall.empty_packet_throttle", Long.toString(1000L / 5)));
    private AtomicLong lastEmptyPacket = new AtomicLong(0);
    private static boolean DIRECT_WARNING;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
    {
        in.markReaderIndex();

        final byte[] buf = new byte[ 3 ];
        for ( int i = 0; i < buf.length; i++ )
        {
            if ( !in.isReadable() )
            {
                in.resetReaderIndex();
                return;
            }

            buf[i] = in.readByte();
            if ( buf[i] >= 0 )
            {
                int length = DefinedPacket.readVarInt( Unpooled.wrappedBuffer( buf ) );
                if ( length == 0 )
                {
                    if (EMPTY_PACKET_THROTTLE > 0) {
                        long currentTime = System.currentTimeMillis();
                        long lastEmptyPacket = this.lastEmptyPacket.getAndSet(currentTime);

                        if (currentTime - lastEmptyPacket < EMPTY_PACKET_THROTTLE) {
                            throw new CorruptedFrameException( "Too many empty packets" );
                        }
                    } else {
                        throw new CorruptedFrameException( "Empty Packet!" );
                    }
                }

                if ( in.readableBytes() < length )
                {
                    in.resetReaderIndex();
                    return;
                } else
                {
                    if ( in.hasMemoryAddress() )
                    {
                        out.add( in.slice( in.readerIndex(), length ).retain() );
                        in.skipBytes( length );
                    } else
                    {
                        if (!DIRECT_WARNING) {
                            DIRECT_WARNING = true;
                            new Throwable("Using a " + in.getClass().getTypeName() + ", not a direct byte buf!").printStackTrace();
                        }

                        // See https://github.com/SpigotMC/BungeeCord/issues/1717
                        ByteBuf dst = ctx.alloc().directBuffer( length );
                        in.readBytes( dst );
                        out.add( dst );
                    }
                    return;
                }
            }
        }

        throw new CorruptedFrameException( "length wider than 21-bit" );
    }
}
