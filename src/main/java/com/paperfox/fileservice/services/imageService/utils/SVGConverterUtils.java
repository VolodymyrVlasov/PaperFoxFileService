package com.paperfox.fileservice.services.imageService.utils;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.render.ps.EPSTranscoder;
import org.apache.fop.render.ps.PSTranscoder;
import org.apache.fop.svg.PDFTranscoder;

import java.io.*;

/**
 * * Use Apache Batik to convert SVG to PDF/PNG/JPG
 */
public class SVGConverterUtils {public void svgtoeps(String svgCode, OutputStream os){

    EPSTranscoder epsTranscoder = new EPSTranscoder();
    try {
        svgCode = svgCode.replaceAll(":rect", "rect");
        TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svgCode.getBytes()));
        TranscoderOutput output = new TranscoderOutput(os);
        epsTranscoder.transcode(input, output);
        os.flush();
        os.close();
    } catch (Exception e) {
    }
}


    /**
     * SVG to PNG
     *
     * @param svgCode SVG code
     * @param outpath output path
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PNG(String svgCode, String outpath) throws TranscoderException, IOException {
        Transcoder transcoder = new PNGTranscoder();
        svgConverte(svgCode, outpath, transcoder);
    }

    /**
     * SVG to PNG
     *
     * @param svgCode SVG code
     * @param out output stream
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PNG(String svgCode, OutputStream out) throws TranscoderException, IOException {
        Transcoder transcoder = new PNGTranscoder();
        svgConverte(svgCode, out, transcoder);
    }

    /**
     * SVG to PNG
     *
     * @param svgFile SVG file
     * @param outFile output file
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PNG(File svgFile, File outFile) throws TranscoderException, IOException {
        Transcoder transcoder = new PNGTranscoder();
        svgConverte(svgFile, outFile, transcoder);
    }

    /**
     * SVG to JPG
     *
     * @param svgCode SVG code
     * @param outpath output path
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2JPEG(String svgCode, String outpath) throws TranscoderException, IOException {
        Transcoder transcoder = new JPEGTranscoder();
        //To prevent ERROR: The JPEG quality has not been specified. Use the default one: no compression error, the following configuration is required
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 0.99f);
        svgConverte(svgCode, outpath, transcoder);
    }

    /**
     * SVG to JPG
     *
     * @param svgCode SVG code
     * @param out output stream
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2JPEG(String svgCode, OutputStream out) throws TranscoderException, IOException {
        Transcoder transcoder = new JPEGTranscoder();
        //To prevent ERROR: The JPEG quality has not been specified. Use the default one: no compression error, the following configuration is required
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 0.99f);
        svgConverte(svgCode, out, transcoder);
    }

    /**
     * SVG to JPG
     *
     * @param svgFile SVG file
     * @param outFile output file
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2JPEG(File svgFile, File outFile) throws TranscoderException, IOException {
        Transcoder transcoder = new JPEGTranscoder();
        //To prevent ERROR: The JPEG quality has not been specified. Use the default one: no compression error, the following configuration is required
        transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, 0.99f);
        svgConverte(svgFile, outFile, transcoder);
    }

    /**
     * SVG to PDF
     *
     * @param svgCode SVG code
     * @param outpath output path
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PDF(String svgCode, String outpath) throws TranscoderException, IOException {
        Transcoder transcoder = new PDFTranscoder();
        svgConverte(svgCode, outpath, transcoder);
    }

    /**
     * SVG to PS
     *
     * @param svgCode
     * @param outpath
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PS(String svgCode, String outpath) throws TranscoderException, IOException {
        Transcoder transcoder = new PSTranscoder();
        svgConverte(svgCode, outpath, transcoder);
    }

    /**
     * SVG to PS
     *
     * @param svgCode SVG code
     * @param out output stream
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PS(String svgCode, OutputStream out) throws TranscoderException, IOException {
        Transcoder transcoder = new PSTranscoder();
        svgConverte(svgCode, out, transcoder);
    }

    /**
     * SVG to EPS
     *
     * @param svgCode SVG code
     * @param out output stream
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2EPS(String svgCode, OutputStream out) throws TranscoderException, IOException {
        Transcoder transcoder = new EPSTranscoder();
        svgConverte(svgCode, out, transcoder);
    }

    /**
     * SVG to EPS
     *
     * @param svgCode
     * @param outpath
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2EPS(String svgCode, String outpath) throws TranscoderException, IOException {
        Transcoder transcoder = new EPSTranscoder();
        svgConverte(svgCode, outpath, transcoder);
    }

    /**
     * SVG to PDF
     *
     * @param svgCode SVG code
     * @param out output stream
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PDF(String svgCode, OutputStream out) throws TranscoderException, IOException {
        Transcoder transcoder = new PDFTranscoder();
        svgConverte(svgCode, out, transcoder);
        out.flush();
        out.close();
    }

    /**
     * SVG to PDF
     *
     * @param svgFile SVG file
     * @param outFile output file
     * @throws TranscoderException
     * @throws IOException
     */
    public void svg2PDF(File svgFile, File outFile) throws TranscoderException, IOException {
        Transcoder transcoder = new PDFTranscoder();
        svgConverte(svgFile, outFile, transcoder);
    }

    private void svgConverte(String svgCode, String outpath, Transcoder transcoder) throws IOException, TranscoderException {
        svgConverte(svgCode, getOutputStream(outpath), transcoder);
    }

    private void svgConverte(File svg, File outFile, Transcoder transcoder) throws IOException, TranscoderException {
        svgConverte(svg2String(getInputStream(svg)), getOutputStream(outFile), transcoder);
    }

    private void svgConverte(String svgCode, OutputStream out, Transcoder transcoder) throws IOException, TranscoderException {
        svgCode = svgCode.replaceAll(":rect", "rect");
        TranscoderInput input = new TranscoderInput(new ByteArrayInputStream(svgCode.getBytes()));
        TranscoderOutput output = new TranscoderOutput(out);
        svgConverte(input, output, transcoder);
    }

    private void svgConverte(TranscoderInput input, TranscoderOutput output, Transcoder transcoder) throws IOException, TranscoderException {
        transcoder.transcode(input, output);
    }

    public InputStream getInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }

    public InputStream getInputStream(String filepath) throws IOException {
        File file = new File(filepath);
        if (file.exists())
            return getInputStream(file);
        else
            return null;
    }

    public OutputStream getOutputStream(File outFile) throws IOException {
        return new FileOutputStream(outFile);
    }

    public OutputStream getOutputStream(String outpath) throws IOException {
        File file = new File(outpath);
        if (!file.exists())
            file.createNewFile();
        return getOutputStream(file);
    }

    /**
     * The default encoding is UTF-8 SVG file input stream String
     *
     * @param svgFile
     * @return SVG code
     * @throws IOException 
     */
    public String svg2String(File svgFile) throws IOException {
        InputStream in = getInputStream(svgFile);
        return svg2String(in, "UTF-8");
    }

    /**
     * SVG file input flow String
     *
     * @param svgFile
     * @return SVG code
     * @throws IOException 
     */
    public String svg2String(File svgFile, String charset) throws IOException {
        InputStream in = getInputStream(svgFile);
        return svg2String(in, charset);
    }

    /**
     * The default encoding is UTF-8SVG input stream String
     *
     * @param in
     * @return SVG code
     */
    public String svg2String(InputStream in) {
        return svg2String(in, "UTF-8");
    }

    /**
     * Specify the character set SVG input stream to String
     *
     * @param in input stream
     * @param charset character encoding
     * @return SVG code
     */
    public String svg2String(InputStream in, String charset) {
        StringBuffer svgBuffer = new StringBuffer();
        BufferedReader bfr = null;
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(in, charset);
            bfr = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bfr.readLine()) != null) {
                svgBuffer.append(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bfr != null)
                    bfr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return svgBuffer.toString();
    }
}